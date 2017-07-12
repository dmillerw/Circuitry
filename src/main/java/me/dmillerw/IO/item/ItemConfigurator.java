package me.dmillerw.io.item;

import me.dmillerw.io.lib.ModInfo;
import me.dmillerw.io.lib.ModTab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class ItemConfigurator extends Item {

    public ItemConfigurator() {
        super();

        setMaxStackSize(1);
        setMaxDamage(0);

        setUnlocalizedName(ModInfo.MOD_ID + ":configurator");

        setCreativeTab(ModTab.TAB);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        /*if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null && tile instanceof TileToolContainer) {
                if (((TileToolContainer) tile).inputs.isEmpty())
                    return EnumActionResult.PASS;

                if (player.isSneaking()) {
                    System.out.println("");
                    return EnumActionResult.SUCCESS;
                }

                Set<IGridMember> checked = Sets.newHashSet();
                Set<IGridMember> connected = Sets.newHashSet();

                for (IGridMember member : ConnectivityGrid.getNeighbors((IGridMember) tile, false)) {
                    if (member.getMemberType() == ConnectivityGrid.MemberType.CABLE) {
                        connected.addAll(member.getGrid().crawlGrid(member, checked, (IGridMember)tile, true));
                    }
                }

                Map<BlockPos, Set<Pair<String, DataType>>> circuits = Maps.newHashMap();

                connected.forEach(m -> {
                    if (m.getMemberType() == ConnectivityGrid.MemberType.NODE) {
                        if (m instanceof TileToolContainer && !((TileToolContainer) m).outputs.isEmpty()) {
                            Set<Pair<String, DataType>> outputs = Sets.newHashSet();
                            ((TileToolContainer) m).outputs.forEach((o, p) -> outputs.add(Pair.of(o, p.type)));
                            circuits.put(((TileToolContainer) m).getPos(), outputs);
                        }
                    }
                });

                circuits.remove(tile.getPos());

                COpenConfiguratorGui packet = new COpenConfiguratorGui();
                packet.networkMembers = circuits;
                packet.blockPos = pos;

                PacketHandler.INSTANCE.sendTo(packet, (EntityPlayerMP) player);
            }
        }*/

        return world.isRemote ? EnumActionResult.PASS : EnumActionResult.SUCCESS;
    }
}
