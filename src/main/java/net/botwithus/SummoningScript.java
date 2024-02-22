package net.botwithus;

import net.botwithus.api.game.hud.Dialog;
import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.api.game.hud.inventories.BackpackInventory;
import net.botwithus.api.game.hud.inventories.Bank;
import net.botwithus.api.game.hud.traversal.Lodestone;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.events.impl.ChatMessageEvent;
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
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.LoopingScript;
import net.botwithus.rs3.script.config.ScriptConfig;
import net.botwithus.rs3.util.Regex;
import net.botwithus.rs3.game.*;


import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.Random;
import java.util.function.Function;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

public class SummoningScript extends LoopingScript {

    private BotState botState = BotState.IDLE;
    private boolean someBool = true;
    private boolean someBool1 = true;
    private boolean someBool2 = true;
    private Random random = new Random();


    private Area TaverleyArea = new Area.Rectangular(new Coordinate(2917,3427,0), new Coordinate(2925,3433,0));
    private Area TaverleyBank = new Area.Rectangular(new Coordinate(2873,3415,0), new Coordinate(2875,3419,0));
    private Area TaverleyArea1 = new Area.Rectangular(new Coordinate(2888,3412,0), new Coordinate(2893,3417,0));
    private Area MehaphosBank = new Area.Rectangular(new Coordinate(3231,2760,0), new Coordinate(3237,2757,0));
    private Area MehaphosArea = new Area.Rectangular(new Coordinate(3225,2765,0), new Coordinate(3230, 2760,0));
    enum BotState {
        //define your own states here
        IDLE,
        SKILLING,
        BANKING,
        //...

    }

    public SummoningScript(String s, ScriptConfig scriptConfig, ScriptDefinition scriptDefinition) {
        super(s, scriptConfig, scriptDefinition);
        this.sgc = new SummoningScriptGraphicsContext(getConsole(), this);
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
                if (someBool == true) {
                    Execution.delay(handleSkilling(player));
                }else if (someBool1 == true)
                {
                    Execution.delay(handleSkillingMenaphos(player));
                }
                else
                {
                    println("Please select the Area");
                }

            }
            case BANKING -> {
                //handle your banking logic, etc\
                if (someBool == true) {
                    Execution.delay(handleBanking(player));
                }else if (someBool1 == true)
                {
                    Execution.delay(handleBankingMenaphos(player));
                }
                else
                {
                    println("Please select the Area");
                }
            }
        }
    }

    private boolean WalkToTaverleyObi(LocalPlayer player )
    {
        if(player.getCoordinate().getRegionId() != 11573)
        {
            println("Player not at Taverley Area");
            println("Region ID" + player.getCoordinate().getRegionId());
            println("Region X" + player.getCoordinate().getX());
            println("Region Y" + player.getCoordinate().getY());
           // Travel.walkTo(3300,3274);


        }
        Coordinate TaverleyAreaRandom= TaverleyArea.getRandomCoordinate();
        Travel.walkTo(TaverleyAreaRandom.getX(), TaverleyAreaRandom.getY());
        Execution.delayUntil(10000,() -> {

            assert player != null;
            return TaverleyArea.contains(player.getCoordinate());
        });


        if(player.getCoordinate().getRegionId() == 11573 )
        {
            println("Taverley Area reached");
            println("Cord X" + player.getCoordinate().getX());
            println("Cord Y" + player.getCoordinate().getY());
            return true;
        }
        else
        {
            println("You are not in the area");
            return false;
        }

    }

    private boolean WalkToTaverleyBank(LocalPlayer player)
    {
        if(player.getCoordinate().getRegionId() != 11573)
        {
            println("Player not at Taverley Area");
            println("Region ID" + player.getCoordinate().getRegionId());
            println("Region X" + player.getCoordinate().getX());
            println("Region Y" + player.getCoordinate().getY());
        }
        Coordinate TaverleyBankAreaRandom= TaverleyArea1.getRandomCoordinate();
        Travel.walkTo(TaverleyBankAreaRandom.getX(), TaverleyBankAreaRandom.getY());
        Execution.delayUntil(5000,() -> {

            assert player != null;
            return TaverleyArea1.contains(player.getCoordinate());
        });
        if(player.getCoordinate().getRegionId() == 11573 )
        {
            println("Taverley Area reached");
            println("Cord X" + player.getCoordinate().getX());
            println("Cord Y" + player.getCoordinate().getY());
            return true;
        }
        else
        {
            println("You are not in the area");
            return false;
        }
    }
    private boolean WalkToMenaphos(LocalPlayer player )
    {
        if(player.getCoordinate().getRegionId() != 12843)
        {
            println("Player not at Menaphos Area");
            println("Region ID" + player.getCoordinate().getRegionId());
            println("Region X" + player.getCoordinate().getX());
            println("Region Y" + player.getCoordinate().getY());
            // Travel.walkTo(3300,3274);


        }
        Coordinate MenaphosAreaRandom= MehaphosArea.getRandomCoordinate();
        Travel.walkTo(MenaphosAreaRandom.getX(), MenaphosAreaRandom.getY());
        Execution.delayUntil(10000,() -> {

            assert player != null;
            return MehaphosArea.contains(player.getCoordinate());
        });


        if(player.getCoordinate().getRegionId() == 12843 )
        {
            println("Menaphos Area reached");
            println("Cord X" + player.getCoordinate().getX());
            println("Cord Y" + player.getCoordinate().getY());
            return true;
        }
        else
        {
            println("You are not in the area");
            return false;
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
            Bank.loadPreset(1);
            botState = BotState.SKILLING;
            return random.nextLong(1000,3000);
        }
        SceneObject BankCounter = SceneObjectQuery.newQuery().name("Counter").option("Bank").results().nearest();
        if (BankCounter == null)
        {
            WalkToTaverleyBank(player);
        }
        else
        {
            ResultSet<SceneObject> banks = SceneObjectQuery.newQuery().name("Counter").option("Bank").inside(TaverleyBank).results();
            if (banks.isEmpty())
            {
                println("Bank query was empty.");
            }
            else
            {
                SceneObject bank = banks.random();
                if (bank != null) {
                    println("Yay, we found our bank.");
                    println("Interacted bank: " + bank.interact("Bank"));
                    //Bank.depositAllExcept(54004);
                }
            }
        }

        return random.nextLong(1500,3000);
    }

    private long handleBankingMenaphos(LocalPlayer player)  // Menaphos Banking//
    {
        println("Player moving 1:" +player.isMoving());

        if(player.isMoving())
        {
            return random.nextLong(3000,5000);
        }
        if (Bank.isOpen())
        {
            println("Bank is open");
            Bank.loadPreset(1);
            botState = BotState.SKILLING;
            return random.nextLong(1000,3000);
        }
        SceneObject Banker = SceneObjectQuery.newQuery().name("Bank booth").option("Bank").results().nearest();
        if (Banker == null)
        {
            WalkToMenaphos(player);
        }
        else
        {
            ResultSet<SceneObject> banks = SceneObjectQuery.newQuery().name("Bank booth").option("Bank").inside(MehaphosBank).results();
            if (banks.isEmpty())
            {
                println("Bank query was empty.");
            }
            else
            {
                SceneObject bank = banks.random();
                if (bank != null) {
                    println("Yay, we found our bank.");
                    println("Interacted bank: " + bank.interact("Bank"));
                    //Bank.depositAllExcept(54004);
                }
            }
        }

        return random.nextLong(1500,3000);
    }

    private long handleSkilling(LocalPlayer player) {
         if (Interfaces.isOpen(1251))
             return random.nextLong(150,3000);


         if (!Backpack.containsAllOf("Pouch","Spirit shards","Blue charm","Rune bar"))
         {

             println("Going to banking state");
             botState = botState.BANKING;
             return random.nextLong(1500,3000);
         }
        println("Player moving:" +player.isMoving());
        println("Region ID" + player.getCoordinate().getRegionId());

        SceneObject Obelisk = SceneObjectQuery.newQuery().name("Obelisk").results().nearest();
         if (Obelisk == null)
         {
             println("Region ID" + player.getCoordinate().getRegionId());
             println("Not in correct Area");
             WalkToTaverleyObi(player);
         }
         else
         {
             println ("Infuse Pouches" + Obelisk.interact("Infuse-pouch"));
             Execution.delay(1000);
             //Dialog.interact("Create");

             println("Craft" + MiniMenu.interact(ComponentAction.DIALOGUE.getType(),0,-1,89784350));
             Execution.delayUntil(2000, () -> !Interfaces.isOpen(1371));
             Execution.delayUntil(2000, () -> !Interfaces.isOpen(1251));
             //botState = botState.BANKING;
             return random.nextLong(1000,2000);

         }



        return random.nextLong(1500,3000);
    }

    private long handleSkillingMenaphos(LocalPlayer player) {
        if (Interfaces.isOpen(1251))
            return random.nextLong(150,3000);


        if (!Backpack.containsAllOf("Pouch","Spirit shards","Blue charm","Rune bar"))
        {

            println("Going to banking state");
            botState = botState.BANKING;
            return random.nextLong(1500,3000);
        }
        println("Player moving:" +player.isMoving());
        println("Region ID" + player.getCoordinate().getRegionId());

        SceneObject Obelisk = SceneObjectQuery.newQuery().name("Obelisk").results().nearest();
        if (Obelisk == null)
        {
            println("Region ID" + player.getCoordinate().getRegionId());
            println("Not in correct Area");
            WalkToMenaphos(player);
        }
        else
        {
            println ("Infuse Pouches" + Obelisk.interact("Infuse-pouch"));
            Execution.delay(1000);
            //Dialog.interact("Create");

            println("Craft" + MiniMenu.interact(ComponentAction.DIALOGUE.getType(),0,-1,89784350));
            Execution.delayUntil(2000, () -> !Interfaces.isOpen(1371));
            Execution.delayUntil(2000, () -> !Interfaces.isOpen(1251));
            //botState = botState.BANKING;
            return random.nextLong(1000,2000);

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

    public boolean isSomeBool1() {
        return someBool1;
    }

    public void setSomeBool1(boolean someBool1) {
        this.someBool1 = someBool1;
    }

    public boolean isSomeBool2() {
        return someBool2;
    }

    public void setSomeBool2(boolean someBool2) {
        this.someBool2 = someBool2;
    }

 /*   private int xpGained()
    {
        xpPerHour = (int) (Math.round((3600.0 / currenttime) * xpGained()))

        subscribe(SkillUpdateEvent.class, skillUpdateEvent ->
        {
            if(skillUpdateEvent.getId() == Skills.MINING.getId())
            {
                xpGained += (skillUpdateEvent.getExperience() - skillUpdateEvent.getOldExperience());
                if (skillUpdateEvent.getOldExperience() < skillUpdateEvent.getActualLevel());
                levelsGained++;
            }
        });

        return xpGained();
    }
*/



}