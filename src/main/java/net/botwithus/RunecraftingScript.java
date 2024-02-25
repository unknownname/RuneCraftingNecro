package net.botwithus;

import net.botwithus.api.game.Items;
import net.botwithus.api.game.hud.Dialog;
import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.api.game.hud.inventories.BackpackInventory;
import net.botwithus.api.game.hud.inventories.Bank;
import net.botwithus.api.game.hud.inventories.BankInventory;
import net.botwithus.api.game.hud.traversal.Lodestone;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.events.impl.ChatMessageEvent;
import net.botwithus.rs3.events.impl.InventoryUpdateEvent;
import net.botwithus.rs3.events.impl.ServerTickedEvent;
import net.botwithus.rs3.events.impl.SkillUpdateEvent;
import net.botwithus.rs3.game.Area;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.Coordinate;
import net.botwithus.rs3.game.Item;
import net.botwithus.rs3.game.actionbar.ActionBar;
import net.botwithus.rs3.game.hud.interfaces.Component;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.js5.types.HeadbarType;
import net.botwithus.rs3.game.js5.types.InventoryType;
import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.ComponentAction;
import net.botwithus.rs3.game.minimenu.actions.MiniMenuAction;
import net.botwithus.rs3.game.queries.builders.ItemQuery;
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery;
import net.botwithus.rs3.game.queries.builders.items.InventoryItemQuery;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.queries.results.ResultSet;
import net.botwithus.rs3.game.scene.entities.characters.Headbar;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.game.skills.Skills;
import net.botwithus.rs3.imgui.ImGui;
import net.botwithus.rs3.imgui.NativeInteger;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.LoopingScript;
import net.botwithus.rs3.script.config.ScriptConfig;
import net.botwithus.rs3.util.Regex;
import net.botwithus.rs3.game.*;


import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.time.Instant;
import java.util.Random;
import java.util.function.Function;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.regex.Pattern;

public class RunecraftingScript extends LoopingScript {

    private BotState botState = BotState.IDLE;
    private boolean someBool = true;

    private Random random = new Random();
    private Pattern Impure = Regex.getPatternForContainingOneOf("Impure Essesnce", "Impure essence");
    private Instant scriptStartTime;
    //NativeInteger currentItem = new NativeInteger(0);
    String[] options = {"Spirit","Bone","Flesh","Miasma"};
    private int currentItem = 0;


    // public int getStartingRunecraftingLevel()
    //{
    //    return startingRunecraftingLevel();
    //}

    public void NecroRunes()
    {
        NativeInteger currentItemNative = new NativeInteger(currentItem);
        if (ImGui.Combo("Rune", currentItemNative,  options))
        {
            currentItem = currentItemNative.get();
            println("Rune Selected" + options[currentItem]);
        }
    }


    private Area TaverleyArea = new Area.Rectangular(new Coordinate(2917,3427,0), new Coordinate(2925,3433,0));

    enum BotState {
        //define your own states here
        IDLE,
        SKILLING,
        BANKING,
        //...

    }

    public RunecraftingScript(String s, ScriptConfig scriptConfig, ScriptDefinition scriptDefinition) {
        super(s, scriptConfig, scriptDefinition);
        this.sgc = new RunecraftingScriptGraphicsContext(getConsole(), this);
    }

    @Override
    public void onLoop() {
        //Loops every 100ms by default, to change:
        //this.loopDelay = 500;
        LocalPlayer player = Client.getLocalPlayer();
        if (player == null || Client.getGameState() != Client.GameState.LOGGED_IN || botState == BotState.IDLE) {
            //wait some time so we dont immediately start on login.
            Execution.delay(random.nextLong(3000,7000));
            return;
        }

        switch (botState) {
            case IDLE -> {
                //do nothing
                println("We're idle!");
                Execution.delay(random.nextLong(1000,3000));
            }
            case SKILLING -> {
                //do some code that handles your skilling
                //Execution.delay(regionIDFinder(player));
                scriptStartTime = Instant.now();
                Execution.delay(handleSkilling(player));
                //println("Selected Rune" + currentItem);
                println("Selected Rune" + options[currentItem]);
                //Execution.delay(handleSkillingMenaphos(player));
            }
            case BANKING -> {
                //handle your banking logic, etc\
                Execution.delay(handleBanking(player));
                //Execution.delay(handleBankingMenaphos(player));
            }
        }


    }

    private long regionIDFinder(LocalPlayer player)
    {

        println("Region ID" + player.getCoordinate().getRegionId());
        return random.nextLong(1500,3000);

    }

    private long handleBanking(LocalPlayer player)  // Taverley Banking//
    {
        println("Player moving 1:" +player.isMoving());

        if(player.isMoving())
        {
            return random.nextLong(3000,5000);
        }
        if (Bank.isOpen())
        {
            println("Bank is open");
            ResultSet<Item> ImpureEssence = InventoryItemQuery.newQuery(95).name("Impure essence").results();
            if(!ImpureEssence.isEmpty())
                for(Item item : ImpureEssence)
            {
               // println("Name: " + item.getName() + "Stack: " + item.getStackSize());

                if (item.getStackSize() > 0)
                {
                    println(" Available Impure essence" + item.getStackSize());
                    Bank.loadPreset(2);
                }
                else
                {
                    println(" Not enough Impure essence");
                    botState = BotState.IDLE;
                }

            }


       //         Bank.loadPreset(2);

            //Bank.loadPreset(2);
            botState = BotState.SKILLING;
            return random.nextLong(1000,3000);
        }
        SceneObject BankChest = SceneObjectQuery.newQuery().name("Bank chest").option("Use").results().nearest();
        if (BankChest != null)
        {
           // WalkToTaverleyBank(player);
            println("Interacted bank: " + BankChest.interact("Use"));
        }

        ResultSet<Item> playerinventory = InventoryItemQuery.newQuery(93).results();
        if (!Backpack.contains(Impure) && player.getCoordinate().getRegionId() == 5150 && player.getAnimationId() ==-1 )
        {
            //println("Player Inventory" + playerinventory);
            println("Action Bar value" + ActionBar.useItem("Tome of Um 2", 1));
            return random.nextLong(1000,2000);
        }


        return random.nextLong(1500,3000);
    }



    private long handleSkilling(LocalPlayer player) {
         if (Interfaces.isOpen(1251))
             return random.nextLong(150,3000);


         if (!Backpack.containsAllOf("Impure essence"))
         {

             println("Going to banking state");
             botState = botState.BANKING;
             //Execution.delayUntil(3000, () -> !Interfaces.isOpen(1251));
             return random.nextLong(1500,3000);
         }
        println("Player moving:" +player.isMoving());
        println("Player Animation ID :" +player.getAnimationId());
        println("Region ID Before Portal Check" + player.getCoordinate().getRegionId());
        println("Direction 1: " + player.getDirection1());
        println("Direction 1: " + player.getDirection2());


        SceneObject Portal = SceneObjectQuery.newQuery().name("Dark portal").option("Enter").results().nearest();
         if (Portal != null && player.getCoordinate().getRegionId() != 5150 && !player.isMoving())
         {
             println("Region ID After Portal Select " + player.getCoordinate().getRegionId());
             println("Interact with portal" + Portal.interact("Enter"));

             return random.nextLong(1000,2000);

         }

         if (player.getCoordinate().getRegionId() == 5150 && options[currentItem] == "Miasma")
        {

            SceneObject Miasma = SceneObjectQuery.newQuery().name("Miasma altar").option("Craft runes").results().nearest();
            if(Miasma !=null )
            {
                println("Interact with Miasma Runes Altar" + Miasma.interact("Craft runes"));
                Execution.delay(2000);
            }
         }else if (player.getCoordinate().getRegionId() == 5150 && options[currentItem] == "Spirit")
         {
             SceneObject Spirit = SceneObjectQuery.newQuery().name("Spirit altar").option("Craft runes").results().nearest();
             if(Spirit !=null )
             {
                 println("Interact with Runes Altar" + Spirit.interact("Craft runes"));
                 Execution.delay(2000);
             }
         }else if (player.getCoordinate().getRegionId() == 5150 && options[currentItem] == "Bone")
         {
             SceneObject Bone = SceneObjectQuery.newQuery().name("Bone altar").option("Craft runes").results().nearest();
             if(Bone !=null )
             {
                 println("Interact with Bone Runes Altar" + Bone.interact("Craft runes"));
                 Execution.delay(2000);
             }
         }else if (player.getCoordinate().getRegionId() == 5150 && options[currentItem] == "Flesh")
         {
             SceneObject Flesh = SceneObjectQuery.newQuery().name("Flesh altar").option("Craft runes").results().nearest();
             if(Flesh !=null )
             {
                 println("Interact with Flesh Runes Altar" + Flesh.interact("Craft runes"));
                 Execution.delay(2000);
             }
         }


        return random.nextLong(1500,3000);
    }


    public BotState getBotState() {
        return botState;
    }

    public void setBotState(BotState botState) {
        this.botState = botState;
    }

    public boolean isSomeBool() {
        return someBool;
    }

    public void setSomeBool(boolean someBool) {
        this.someBool = someBool;
    }
}