package net.byAqua3.avaritia.block;

import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;

import net.byAqua3.avaritia.inventory.MenuInfinityChest;
import net.byAqua3.avaritia.tile.TileInfinityChest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockInfinityChest extends BaseEntityBlock implements EntityBlock {

	public static final Component TITLE = Component.translatable("avaritia:container.infinity_chest.title");

	public static final MapCodec<BlockInfinityChest> CODEC = simpleCodec(
			properties -> new BlockInfinityChest(properties));

	public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
	public static final EnumProperty<ChestType> TYPE = BlockStateProperties.CHEST_TYPE;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final int EVENT_SET_OPEN_COUNT = 1;
	protected static final int AABB_OFFSET = 1;
	protected static final int AABB_HEIGHT = 14;
	protected static final VoxelShape NORTH_AABB = Block.box(1.0, 0.0, 0.0, 15.0, 14.0, 15.0);
	protected static final VoxelShape SOUTH_AABB = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 16.0);
	protected static final VoxelShape WEST_AABB = Block.box(0.0, 0.0, 1.0, 15.0, 14.0, 15.0);
	protected static final VoxelShape EAST_AABB = Block.box(1.0, 0.0, 1.0, 16.0, 14.0, 15.0);
	protected static final VoxelShape AABB = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);

	public BlockInfinityChest(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH)
				.setValue(TYPE, ChestType.SINGLE).setValue(WATERLOGGED, Boolean.valueOf(false)));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileInfinityChest(pos, state);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockState updateShape(BlockState p_51555_, LevelReader p_374487_, ScheduledTickAccess p_374060_,
			BlockPos p_51559_, Direction p_51556_, BlockPos p_51560_, BlockState p_51557_, RandomSource p_374212_) {
		if (p_51555_.getValue(WATERLOGGED)) {
			p_374060_.scheduleTick(p_51559_, Fluids.WATER, Fluids.WATER.getTickDelay(p_374487_));
		}

		if (p_51557_.is(this) && p_51556_.getAxis().isHorizontal()) {
			ChestType chesttype = p_51557_.getValue(TYPE);
			if (p_51555_.getValue(TYPE) == ChestType.SINGLE && chesttype != ChestType.SINGLE
					&& p_51555_.getValue(FACING) == p_51557_.getValue(FACING)) {
				return p_51555_.setValue(TYPE, chesttype.getOpposite());
			}
		} else {
			return p_51555_.setValue(TYPE, ChestType.SINGLE);
		}

		return super.updateShape(p_51555_, p_374487_, p_374060_, p_51559_, p_51556_, p_51560_, p_51557_, p_374212_);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		ChestType chesttype = ChestType.SINGLE;
		Direction direction = context.getHorizontalDirection().getOpposite();
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());

		return this.defaultBlockState().setValue(FACING, direction).setValue(TYPE, chesttype).setValue(WATERLOGGED,
				Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Nullable
	private Direction candidatePartnerFacing(BlockPlaceContext context, Direction direction) {
		BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().relative(direction));
		return blockstate.is(this) && blockstate.getValue(TYPE) == ChestType.SINGLE ? blockstate.getValue(FACING)
				: null;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> entityType) {
		if (!level.isClientSide()) {
			return null;
		}
		return (world, blockPos, blockState, tile) -> ((TileInfinityChest) tile).update();
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		BlockState rotated = state.rotate(mirror.getRotation(state.getValue(FACING)));
		return mirror == Mirror.NONE ? rotated : rotated.setValue(TYPE, rotated.getValue(TYPE).getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, TYPE, WATERLOGGED);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
			BlockHitResult result) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		} else {
			if (blockEntity instanceof TileInfinityChest) {
				TileInfinityChest tile = (TileInfinityChest) blockEntity;
				SimpleMenuProvider simpleMenuProvider = new SimpleMenuProvider(
						(id, inventory, access) -> new MenuInfinityChest(id, inventory, tile), TITLE);

				player.openMenu(simpleMenuProvider, pos);
			}
			return InteractionResult.CONSUME;
		}
	}

}
