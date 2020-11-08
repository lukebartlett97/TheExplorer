package theExplorer.util;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theExplorer.characters.TheExplorer;
import theExplorer.powers.CompanionPower;

import java.lang.reflect.InvocationTargetException;

public class CompanionService {
    public static final Logger logger = LogManager.getLogger(CompanionService.class.getName());

    public static String getCompanionClass() {
        if (AbstractDungeon.player instanceof TheExplorer) {
            TheExplorer player = (TheExplorer) AbstractDungeon.player;
            return player.getCompanion().getClass().getName();
        }
        return null;
    }

    public static void setCompanion(String companionClass) {
        AbstractPlayer player = AbstractDungeon.player;
        if (player instanceof TheExplorer) {
            TheExplorer explorer = (TheExplorer) player;
            try {
                Class<? extends CompanionPower> aClass = (Class<? extends CompanionPower>) Class.forName(companionClass);
                CompanionPower power = null;
                try {
                    power = aClass.getDeclaredConstructor(AbstractCreature.class, AbstractCreature.class).newInstance(explorer, explorer);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
                if (power != null) {
                    explorer.setCompanion(power);
                }
            } catch (ClassNotFoundException e) {
                logger.error("Class not found for saved companion: " + companionClass);
                e.printStackTrace();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    public static CompanionPower getCompanionPower() {
        if (AbstractDungeon.player != null && AbstractDungeon.player.powers != null) {
            for (AbstractPower power : AbstractDungeon.player.powers) {
                if (power instanceof CompanionPower) {
                    return (CompanionPower) power;
                }
            }
        }

        return null;
    }
}
