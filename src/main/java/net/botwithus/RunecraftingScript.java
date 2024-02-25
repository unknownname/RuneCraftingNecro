package net.botwithus;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.api.game.hud.inventories.Bank;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.game.Area;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.Coordinate;
import net.botwithus.rs3.game.Item;
import net.botwithus.rs3.game.actionbar.ActionBar;
import net.botwithus.rs3.game.hud.interfaces.Interfaces;
import net.botwithus.rs3.game.queries.builders.items.InventoryItemQuery;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.queries.results.ResultSet;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.game.vars.VarManager;
import net.botwithus.rs3.imgui.ImGui;
import net.botwithus.rs3.imgui.NativeInteger;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.LoopingScript;
import net.botwithus.rs3.script.config.ScriptConfig;
import net.botwithus.rs3.util.Regex;


import java.time.Instant;
import java.util.Random;
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


                //println("Small Pouch Large" +VarManager.getVarbitValue(16499));
                //println("Small Pouch Gaint" +VarManager.getVarbitValue(16500));
                //println("Small Pouch Medium" +VarManager.getVarbitValue(16498));
                //println("Small Pouch Small" +VarManager.getVarbitValue(16497));
                //checkRunePouchesAndHandleBanking(player);
                //println("Small Pouch Empty" +VarManager.getVarbitValue(3218));
                //println("Small  Var Domain" + VarManager.getVarDomain(3214));
                //println("Small  Var Fill1" + VarManager.getVarValue(VarDomainType.PLAYER,3214));
                //println("Small  Var Empty2" + VarManager.getVarValue(VarDomainType.PLAYER,3215));


            }
            case BANKING -> {
                //handle your banking logic, etc\
                Execution.delay(handleBanking(player));

            }
        }


    }

    private long regionIDFinder(LocalPlayer player)
    {

        println("Region ID" + player.getCoordinate().getRegionId());
        return random.nextLong(1500,3000);

    }

    private long handleBanking(LocalPlayer player)
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


         if (!Backpack.containsAllOf("Impure essence") && !checkRunePouchesAndHandleBanking())
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


    public boolean checkRunePouchesAndHandleBanking()
    {


        InventoryItemQuery runePouchQuery = InventoryItemQuery.newQuery().ids(5514,5509,5510,5512);
        ResultSet<Item> runePouches = runePouchQuery.results();
        boolean hasRunes = false;

        if(!runePouches.isEmpty())
        {
            for (Item pouch : runePouches)
            {
                boolean containsRunes = checkPouchForRunes(pouch, 55667);
                if(containsRunes)
                {
                    println("Rune pouch contains runes");
                }
                else
                {
                    println("Pouch contains no runes");
                }
            }

        }
        println("No rune pouch founded in the inventory");
        return false;

    }

    private boolean isRunePouch(Item item)
    {
        InventoryItemQuery pouch = InventoryItemQuery.newQuery().ids(5514);
        if (Backpack.contains(5514)) {

            println("There is rune pouch in the inventory");
            return true;
        }

        return false;
    }

    private boolean checkPouchForRunes (Item pouch, int i)
    {
        //int GaintPouchVarbitID =16500;
        //ResultSet<Item> GaintRunePouch = InventoryItemQuery.newQuery().ids(5514).results();
        int SmallPouchValue = VarManager.getVarbitValue(16497);
        int MediumPouchValue = VarManager.getVarbitValue(16498);
        int LargePouchValue = VarManager.getVarbitValue(16499);
        int GaintPouchValue = VarManager.getVarbitValue(16500);
        if (GaintPouchValue > 0 && SmallPouchValue >0 && MediumPouchValue >0 && LargePouchValue >0)
        {
            println("The Small rune pouch contains runes:"  + VarManager.getVarbitValue(16497));
            println("The Medium rune pouch contains runes:"  + VarManager.getVarbitValue(16498));
            println("The Large rune pouch contains runes:"  + VarManager.getVarbitValue(16499));
            println("The Gaint rune pouch contains runes:"  + VarManager.getVarbitValue(16500));
            return true;
        }
        return false;

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