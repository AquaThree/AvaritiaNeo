package net.byAqua3.avaritia.block;

import java.util.function.Supplier;

import net.byAqua3.avaritia.tile.TileMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class BlockMachine extends Block implements EntityBlock {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
	private static final Property<Direction> FACING = BlockStateProperties.FACING;

	private final Supplier<BlockEntityType<? extends TileMachine>> supplier;

	public BlockMachine(Properties properties, Supplier<BlockEntityType<? extends TileMachine>> supplier) {
		super(properties);
		this.supplier = supplier;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return this.supplier.get().create(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> blockEntityType) {
		if (level.isClientSide()) {
			return null;
		}
		return (world, blockPos, blockState, tile) -> ((TileMachine) tile).updateServer();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(new Property[] { ACTIVE, FACING });
	}

}
