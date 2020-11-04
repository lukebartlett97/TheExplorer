package theExplorer.util;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import theExplorer.cards.plants.BlisteringPlant;
import theExplorer.cards.plants.ScaledPlant;
import theExplorer.cards.plants.SpikyPlant;
import theExplorer.cards.plants.ToxicPlant;

import java.util.ArrayList;

public class PlantService {
    private static ArrayList<Class> plants = new ArrayList();

    private static void initializePlants() {
        if (plants.isEmpty()) {
            plants.add(SpikyPlant.class);
            plants.add(ToxicPlant.class);
            plants.add(ScaledPlant.class);
            plants.add(BlisteringPlant.class);
        }
    }

    public static CustomCard getRandomPlant(boolean seeded) {
        initializePlants();
        Random rng = seeded ? AbstractDungeon.cardRng : new Random();
        int roll = rng.random(0, plants.size() - 1);
        try {
            return (CustomCard) plants.get(roll).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return new SpikyPlant();
    }

    //Needs to be seeded KEKW
    public static CustomCard getRandomPlant() {
        return getRandomPlant(true);
    }

    public static ArrayList<CustomCard> getAllPlants(boolean upgraded) {
        initializePlants();
        ArrayList<CustomCard> retval = new ArrayList<>();
        try {
            for (Class plantClass : plants) {
                CustomCard plant = (CustomCard) plantClass.newInstance();
                if (upgraded) {
                    plant.upgrade();
                }
                retval.add(plant);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return retval;
    }

    public static boolean isPlant(AbstractCard card) {
        return plants.contains(card.getClass());
    }
}
