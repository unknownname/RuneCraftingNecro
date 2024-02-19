package net.botwithus;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.api.game.hud.inventories.Bank;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.events.impl.ChatMessageEvent;
import net.botwithus.rs3.events.impl.ServerTickedEvent;
import net.botwithus.rs3.game.Area;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.Coordinate;
import net.botwithus.rs3.game.Item;
import net.botwithus.rs3.game.hud.interfaces.Component;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery;
import net.botwithus.rs3.game.queries.builders.items.InventoryItemQuery;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.queries.results.ResultSet;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.game.skills.Skills;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.LoopingScript;
import net.botwithus.rs3.script.config.ScriptConfig;
import net.botwithus.rs3.util.Regex;


import java.util.Random;

public class CraftingScript extends LoopingScript {

    private BotState botState = BotState.IDLE;
    private boolean someBool = true;
    private Random random = new Random();
    private Area AlKharid = new Area.Rectangular(new Coordinate(3274,3168,0), new Coordinate(3267,3170,0));

    enum BotState {
        //define your own states here
        IDLE,
        SKILLING,
        BANKING,
        //...

    }

    public CraftingScript(String s, ScriptConfig scriptConfig, ScriptDefinition scriptDefinition) {
        super(s, scriptConfig, scriptDefinition);
        this.sgc = new CraftingScriptGraphicsContext(getConsole(), this);
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
                Execution.delay(handleSkilling(player));
            }
            case BANKING -> {
                //handle your banking logic, etc
                Execution.delay(handleBanking(player));
            }
        }
    }


    private long handleBanking(LocalPlayer player)
    {
        println("Player moving:" +player.isMoving());
        if(player.isMoving())
        {
            return random.nextLong(3000,5000);
        }
        if (Bank.isOpen())
        {
            println("Bank is open");

            botState = BotState.SKILLING;
        }
        else
        {
            ResultSet<SceneObject> banks = SceneObjectQuery.newQuery().name("Bank booth ").option("Bank").inside(AlKharid).id(76274).results();
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
                }
            }
        }

        return random.nextLong(1500,3000);
    }



    private long handleSkilling(LocalPlayer player) {
         if (Interfaces.isOpen(1251))
             return random.nextLong(150,3000);
         if (Backpack.isFull())
         {
             println("Going to banking state");
             botState = botState.BANKING;
             return random.nextLong(1500,3000);
         }
        println("Player moving:" +player.isMoving());
        println("Animation ID: " +  player.getAnimationId());
         if (player.getAnimationId() != -1 || player.isMoving()){
          return random.nextLong(3000,5000);
         }

        SceneObject CommonGem = SceneObjectQuery.newQuery().name("Common gem rock").option("Mine").id(113036).results().nearest();
        if (CommonGem != null) {
            println("Interacted CommonGem: " + CommonGem.interact("Mine"));
        }

        return random.nextLong(1500,3000);
    }

 /*   public int getAdditionalWoodboxCapacity() {
        int level = Skills.WOODCUTTING.getActualLevel();
        for (int threshold = 95; threshold > 0; threshold -= 10) {
            if (level >= threshold)
                return threshold + 5;
        }
        return 0;
    } */

 /*   public int getBaseWoodboxCapacity(String woodboxName) {
        switch (woodboxName) {
            case "Wood box":
                return 70;
            case "Oak wood box":
                return 80;
            case "Willow wood box":
                return 90;
            case "Teak wood box":
                return 100;
            case "Maple wood box":
                return 110;
            case "Acadia wood box":
                return 120;
            case "Mahogany wood box":
                return 130;
            case "Yew wood box":
                return 140;
            case "Magic wood box":
                return 150;
            case "Elder wood box":
                return 160;
        }
        return 0;
    }
*/
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