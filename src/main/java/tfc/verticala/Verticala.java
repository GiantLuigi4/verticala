package tfc.verticala;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

public class Verticala implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
	public static final String MOD_ID = "verticala";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
	}

	@Override
	public void beforeGameStart() {
	}

	@Override
	public void afterGameStart() {
	}

	@Override
	public void onRecipesReady() {
	}
}
