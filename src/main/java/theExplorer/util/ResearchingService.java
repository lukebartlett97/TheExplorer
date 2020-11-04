package theExplorer.util;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theExplorer.characters.TheExplorer;

import java.util.Arrays;

public class ResearchingService {
    public static final Logger logger = LogManager.getLogger(ResearchingService.class.getName());

    public static Integer getAmount() {
        if (AbstractDungeon.player instanceof TheExplorer) {
            TheExplorer player = (TheExplorer) AbstractDungeon.player;
            return player.getResearchStacksPower().amount;
        }
        return 0;
    }

    public static void setAmount(Integer amount) {
        if (AbstractDungeon.player instanceof TheExplorer) {
            TheExplorer player = (TheExplorer) AbstractDungeon.player;
            player.getResearchStacksPower().amount = amount;
        }
    }

    public static String[] getKilled() {
        if (AbstractDungeon.player instanceof TheExplorer) {
            TheExplorer player = (TheExplorer) AbstractDungeon.player;
            return Arrays.stream(player.getResearchStacksPower().killed.toArray()).map(x -> ((Class) x).getName()).toArray(String[]::new);
        }
        return ArrayUtils.EMPTY_STRING_ARRAY;
    }

    public static void setKilled(String[] savedKilled) {
        if (AbstractDungeon.player instanceof TheExplorer) {
            TheExplorer player = (TheExplorer) AbstractDungeon.player;
            for (String killed : savedKilled) {
                try {

                    if (Class.forName(killed).isAssignableFrom(AbstractCreature.class)) {
                        //noinspection unchecked
                        player.getResearchStacksPower().killed.add((Class<? extends AbstractCreature>) Class.forName(killed));
                    } else {
                        logger.error("Saved killed class does not extend abstract monster: " + killed);
                    }
                } catch (ClassNotFoundException | ClassCastException e) {
                    e.printStackTrace();
                    logger.error("Class not found for saved killed monster: " + killed);
                }

            }
        }
    }
}
