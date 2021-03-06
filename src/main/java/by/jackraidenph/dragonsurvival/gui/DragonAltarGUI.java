package by.jackraidenph.dragonsurvival.gui;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.handlers.DragonFoodHandler;
import by.jackraidenph.dragonsurvival.handlers.EventHandler;
import by.jackraidenph.dragonsurvival.models.MagicalPredatorModel;
import by.jackraidenph.dragonsurvival.network.GiveNest;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import by.jackraidenph.dragonsurvival.network.SyncCapabilityDebuff;
import by.jackraidenph.dragonsurvival.network.SynchronizeDragonCap;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;

import java.awt.Container;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.model.BakedItemModel;
import net.minecraftforge.client.model.SeparatePerspectiveModel.BakedModel;
import net.minecraftforge.client.model.obj.OBJModel.ModelObject;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class DragonAltarGUI extends Screen {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(DragonSurvivalMod.MODID, "textures/gui/dragon_altar_texture.png");
    private final int xSize = 852 / 2;
    private final int ySize = 500 / 2;
    private int guiLeft;
    private int guiTop;

    public DragonAltarGUI(ITextComponent title) {
        super(title);
        AltarDragonInfo();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.minecraft == null)
            return;
        this.renderBackground(matrixStack);
        int startX = this.guiLeft;
        int startY = this.guiTop;
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        this.minecraft.getTextureManager().bind(BACKGROUND_TEXTURE);

        blit(matrixStack, startX, startY, 0, 0, 215, 158, 512, 512);


        if (mouseY > startY + 6 && mouseY < startY + 153) {
            if (mouseX > startX + 5 && mouseX < startX + 55) {
                blit(matrixStack, startX + 6, startY + 6, 217, 0, 49, 149, 512, 512);
                if((mouseY > startY + 6 && mouseY < startY + 26)||(mouseY > startY + 133 && mouseY < startY + 153)) {
                	renderWrappedToolTip(matrixStack, AltarDragonInfoCaveDragon, mouseX, mouseY, font);
                }
            }

            if (mouseX > startX + 57 && mouseX < startX + 107) {
                blit(matrixStack, startX + 58, startY + 6, 266, 0, 49, 149, 512, 512);
                if((mouseY > startY + 6 && mouseY < startY + 26)||(mouseY > startY + 133 && mouseY < startY + 153)) {
                	renderWrappedToolTip(matrixStack, AltarDragonInfoForestDragon, mouseX, mouseY, font);
                }
            }

            if (mouseX > startX + 109 && mouseX < startX + 159) {
                blit(matrixStack, startX + 110, startY + 6, 315, 0, 49, 149, 512, 512);
                if((mouseY > startY + 6 && mouseY < startY + 26)||(mouseY > startY + 133 && mouseY < startY + 153)) {
                	renderWrappedToolTip(matrixStack, AltarDragonInfoSeaDragon, mouseX, mouseY, font);
                }
            }

            if (mouseX > startX + 161 && mouseX < startX + 211) {
                blit(matrixStack, startX + 161, startY + 6, 364, 0, 49, 149, 512, 512);
                if((mouseY > startY + 6 && mouseY < startY + 26)||(mouseY > startY + 133 && mouseY < startY + 153)) {
                	renderWrappedToolTip(matrixStack, AltarDragonInfoHuman, mouseX, mouseY, font);
                }
            }
            //warning
//            if(mouseX>startX+5 && mouseX<startX+211) {
//                fill(startX + 8, startY + 166, guiLeft + 210, guiTop + 242, 0xff333333);
//                String warning =TextFormatting.RED + new TranslationTextComponent( "ds.dragon_altar_warning1").getString()+ TextFormatting.RESET + new TranslationTextComponent("ds.dragon_altar_warning2").getString();
//                font.drawSplitString(warning, startX + 10, startY + 153 + 20, 200, 0xffffff);
//            }
        }
    }

    @Override
    protected void init() {
        super.init();

        this.guiLeft = (this.width - this.xSize / 2) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        this.addButton(new ExtendedButton(this.guiLeft + 6, this.guiTop + 6, 49, 147, new StringTextComponent("CAVE"),
                button -> {
                    initiateDragonForm(DragonType.CAVE);
                    minecraft.player.sendMessage(new TranslationTextComponent("ds.cave_dragon_choice"), minecraft.player.getUUID());
                })
        );

        this.addButton(new ExtendedButton(this.guiLeft + 58, this.guiTop + 6, 49, 147, new StringTextComponent("FOREST"),
                button -> {
                    initiateDragonForm(DragonType.FOREST);
                    minecraft.player.sendMessage(new TranslationTextComponent("ds.forest_dragon_choice"), minecraft.player.getUUID());
                })

        );

        this.addButton(new ExtendedButton(this.guiLeft + 110, this.guiTop + 6, 49, 147, new StringTextComponent("SEA"),
                button -> {
                    initiateDragonForm(DragonType.SEA);
                    minecraft.player.sendMessage(new TranslationTextComponent("ds.sea_dragon_choice"), minecraft.player.getUUID());
                })

        );

        addButton(new ExtendedButton(guiLeft + 162, guiTop + 6, 49, 147, new StringTextComponent("Human"), b -> {
            DragonStateProvider.getCap(minecraft.player).ifPresent(playerStateHandler -> {
                playerStateHandler.setIsHiding(false);
                playerStateHandler.setType(DragonType.NONE);
                playerStateHandler.setHasWings(false);
                playerStateHandler.setSize(20F);
                DragonSurvivalMod.CHANNEL.sendToServer(new SynchronizeDragonCap(minecraft.player.getId(), false, DragonType.NONE, 20, false, ConfigHandler.SERVER.caveLavaSwimmingTicks.get(), 0));
                minecraft.player.closeContainer();
                minecraft.player.sendMessage(new TranslationTextComponent("ds.choice_human"), minecraft.player.getUUID());
            });
        }));
    }

    private void initiateDragonForm(DragonType type) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null)
            return;
        player.closeContainer();
        DragonStateProvider.getCap(player).ifPresent(cap -> {
            DragonSurvivalMod.CHANNEL.sendToServer(new SynchronizeDragonCap(player.getId(), false, type, DragonLevel.BABY.size, ConfigHandler.SERVER.startWithWings.get(), ConfigHandler.SERVER.caveLavaSwimmingTicks.get(), 0));
            //DragonSurvivalMod.CHANNEL.sendToServer(new GiveNest(type)); //gives a nest to the player after transforming into a dragon
            player.level.playSound(player, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 1, 0.7f);
            cap.setType(type);
            cap.setSize(DragonLevel.BABY.size, player);
            cap.setHasWings(false);
        });
    }
    
    
    private ArrayList<ITextComponent> AltarDragonInfoCaveDragon;    
    private ArrayList<ITextComponent> AltarDragonInfoForestDragon;    
    private ArrayList<ITextComponent> AltarDragonInfoSeaDragon;    
    private ArrayList<ITextComponent> AltarDragonInfoHuman;
    private List<Item> AltarDragonInfoCaveDragonFood;
    private List<Item> AltarDragonInfoForestDragonFood;
    private List<Item> AltarDragonInfoSeaDragonFood;
    private void AltarDragonInfo(){
    	
    	if(AltarDragonInfoCaveDragonFood==null) {
    		AltarDragonInfoCaveDragonFood = DragonFoodHandler.getSafeEdibleFoods(DragonType.CAVE);
    		AltarDragonInfoForestDragonFood = DragonFoodHandler.getSafeEdibleFoods(DragonType.FOREST);
    		AltarDragonInfoSeaDragonFood = DragonFoodHandler.getSafeEdibleFoods(DragonType.SEA);
    	}
    	AltarDragonInfoCaveDragon = AltarDragonInfoLang("cave_dragon", AltarDragonInfoCaveDragonFood);
    	AltarDragonInfoForestDragon = AltarDragonInfoLang("forest_dragon", AltarDragonInfoForestDragonFood);
    	AltarDragonInfoSeaDragon = AltarDragonInfoLang("sea_dragon", AltarDragonInfoSeaDragonFood);
    	AltarDragonInfoHuman = AltarDragonInfoLang("human", new ArrayList<Item>());
    }
    private ArrayList<ITextComponent> AltarDragonInfoLang(String DragonType, List<Item> Food){
    	ArrayList<ITextComponent> a = new ArrayList<ITextComponent>();
    	String b = "ds.altar_dragon_info."+DragonType+".";
    	int i = 0;
    	if(new TranslationTextComponent(b+i).getString().equals(b+i)) {
    		a.add(ITextComponent.nullToEmpty("none"));
    	}else {
    		boolean c = true;
    		while(c) {
    			String d = new TranslationTextComponent(b+i).getString();
    			if(d.equals(b+i)) {
    				c = false;
    			}else {
    				if(d.indexOf("--food--") > -1) {
    			    	if(DragonType != "human") {
    			    		String food = "";
    						for(Item item : Food) {
    							food += (item.getName(new ItemStack(item)).getString() + "; ");
    						}
    						a.add(ITextComponent.nullToEmpty(d.replaceAll("--food--", food)));
    					}else {
    						a.add(ITextComponent.nullToEmpty(d));
    					}
    				}else {
        				a.add(ITextComponent.nullToEmpty(d));
    				}
    				i++;
    			}
    		}
    	}
    	return a;
    }
}
