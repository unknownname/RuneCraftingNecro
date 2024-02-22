package net.botwithus;

import net.botwithus.rs3.events.impl.SkillUpdateEvent;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.skills.Skill;
import net.botwithus.rs3.imgui.ImGui;
import net.botwithus.rs3.imgui.ImGuiWindowFlag;
import net.botwithus.rs3.script.ScriptConsole;
import net.botwithus.rs3.script.ScriptGraphicsContext;
import net.botwithus.rs3.game.skills.Skills;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;

public class SummoningScriptGraphicsContext extends ScriptGraphicsContext {

    private SummoningScript script;

    public SummoningScriptGraphicsContext(ScriptConsole scriptConsole, SummoningScript script) {
        super(scriptConsole);
        this.script = script;
    }


    @Override
    public void drawSettings() {
        if (ImGui.Begin("Summoning", ImGuiWindowFlag.None.getValue())) {
            if (ImGui.BeginTabBar("Bar", ImGuiWindowFlag.None.getValue())) {
                if (ImGui.BeginTabItem("Play", ImGuiWindowFlag.None.getValue())) {
                    ImGui.Text("Welcome to Pouch Crafting Script");
                    ImGui.Text("Scripts state is: " + script.getBotState());
                    if (ImGui.Button("Start")) {
                        //button has been clicked
                        script.setBotState(SummoningScript.BotState.SKILLING);
                    }
                    ImGui.SameLine();
                    if (ImGui.Button("Stop")) {
                        //has been clicked
                        script.setBotState(SummoningScript.BotState.IDLE);
                    }
                    ImGui.EndTabItem();
                }
                if (ImGui.BeginTabItem("Stats", ImGuiWindowFlag.None.getValue())) {
                    ImGui.Text("Summoning");
                    ImGui.Text("Summoning Level: " + Skills.SUMMONING.getLevel());
                    ImGui.Text("Remaining XP to Next level: " + Skills.SUMMONING.getExperienceToNextLevel());

                    ImGui.EndTabItem();
                }
                if (ImGui.BeginTabItem("Config", ImGuiWindowFlag.None.getValue())) {
                    ImGui.Text("Please Select Summoning Area");
                    script.setSomeBool(ImGui.Checkbox("Taverley", script.isSomeBool()));
                    script.setSomeBool1(ImGui.Checkbox("Menaphos", script.isSomeBool1()));
                    script.setSomeBool2(ImGui.Checkbox("Priff", script.isSomeBool2()));
                    ImGui.EndTabItem();
                }
                ImGui.EndTabBar();
            }
            ImGui.End();
        }

    }

    @Override
    public void drawOverlay() {
        super.drawOverlay();
    }
}

