package theExplorer.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuestPower extends ExplorerPower {
    private static final Logger logger = LogManager.getLogger(QuestPower.class.getName());
    protected int countdown;

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.countdown), x, y, this.fontScale, c);
    }


    @Override
    public void updateDescription() {
        if (descriptions == null) {
            this.description = "MISSING_DESCRIPTION";
            return;
        }
        if (descriptions.length == 1) {
            this.description = descriptions[0];
        } else {
            this.description = descriptions[0] + this.countdown + descriptions[1];
        }
    }

}
