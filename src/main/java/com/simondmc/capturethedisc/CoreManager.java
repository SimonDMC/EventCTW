package com.simondmc.capturethedisc;

import com.nametbd.core.api.CoreTeam;
import com.nametbd.core.api.GameManager;
import com.simondmc.capturethedisc.game.GameCore;
import com.simondmc.capturethedisc.game.Teams;
import com.simondmc.capturethedisc.map.Map;
import com.simondmc.capturethedisc.region.Region;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class CoreManager extends GameManager {

    @Override
    public String getId() {
        return "capture_the_disc";
    }

    @Override
    public Location getLobbyLocation() {
        Location l = Region.LOBBY.clone();
        World w;
        try {
            w = Bukkit.getWorld("ctd-world");
        } catch (NullPointerException e) {
            Map.createMap();
            w = Bukkit.getWorld("ctd-world");
        }
        l.setWorld(w);
        return null;
    }

    @Override
    public List<CoreTeam> startGame() {
        List<CoreTeam> coreTeams = new ArrayList<>();
        // assign teams randomly
        List<Player>[] teams = Teams.assignTeams();
        coreTeams.add(new CoreTeam(teams[0], ChatColor.RED, "Red"));
        coreTeams.add(new CoreTeam(teams[1], ChatColor.GREEN, "Green"));
        GameCore.setup();
        GameCore.startGame();
        return coreTeams;
    }

    @Override
    public void cleanupGame() {
        Map.createMap();
    }

    @Override
    public void setupGame(Runnable finishConsumer) {
        for (Player p : CaptureTheDisc.plugin.getServer().getOnlinePlayers()) {
            Location l = Region.LOBBY.clone();
            l.setWorld(Bukkit.getWorld("ctd-world"));
            p.teleport(l);
            p.setGameMode(GameMode.ADVENTURE);
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            p.getInventory().setItemInOffHand(null);
            p.setHealth(20);
            p.setFoodLevel(20);
            p.setSaturation(20);
            p.setExp(0);
            p.setLevel(0);
            p.setFireTicks(0);
            p.setFallDistance(0);
            p.setAllowFlight(false);
            p.setFlying(false);
            p.setBedSpawnLocation(l, true);
            for (PotionEffect eff : p.getActivePotionEffects()) {
                p.removePotionEffect(eff.getType());
            }
        }
    }

    @Override
    public boolean setupRequired() {
        return true;
    }
}
