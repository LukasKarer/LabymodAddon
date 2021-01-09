package at.lukas.eagle;

import net.labymod.api.LabyModAPI;
import net.labymod.api.LabyModAddon;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.KeyElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class Mod extends LabyModAddon {

    public static KeyBinding keybinding = new KeyBinding("FastBridgeToggle", 66, "FastBridge");
    private static String key = "";
    private static boolean active = false;
    Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void onEnable() {
        System.out.println("FastBridge active");
        ClientRegistry.registerKeyBinding(keybinding);
        this.getApi().registerForgeListener(this);
        //MinecraftForge.EVENT_BUS.register(new Events());
    }

    @Override
    public void loadConfig() {

    }

    @Override
    protected void fillSettings(final List<SettingsElement> subSettings) {
        final BooleanElement booleanElement = new BooleanElement("Enabled", new ControlElement.IconData(Material.EMERALD), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean enabled) {
                active = enabled;
            }
        }, this.active);
        subSettings.add(booleanElement);

       /* KeyElement keyElement = new KeyElement("KeyBind", new ControlElement.IconData(Material.SANDSTONE), new Consumer<Integer>() {
            @Override
            public void accept( Integer accepted ) {
                if ( accepted == -1 ) {
                    System.out.println( "Set new key to NONE" );
                    return;
                }
                key = Keyboard.getKeyName( accepted );

                System.out.println( "Set new key: " + Keyboard.getKeyName( accepted ) );
            }
        });*/
    }


    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent e) {
        if(active) {
            if (Mod.keybinding.isKeyDown()) {
                BlockPos p = (new BlockPos((Entity)this.mc.thePlayer)).add(0, -1, 0);
                boolean b = (this.mc.theWorld.getBlockState(p).getBlock() == Blocks.air);
                if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (!b)
                        setJump(false);
                    setSneak((this.mc.thePlayer.onGround || b));
                    if (!this.mc.thePlayer.onGround) {
                        this.mc.thePlayer.motionX = 0.0D;
                        this.mc.thePlayer.motionZ = 0.0D;
                    }
                } else {
                    setSneak(b);
                }
            }
        }
    }

    public void setSneak(boolean value) {
        this.mc.thePlayer.movementInput.sneak = value;
        KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), value);
    }

    public void setJump(boolean value) {
        this.mc.thePlayer.movementInput.jump = value;
        KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindJump.getKeyCode(), value);
    }
}
