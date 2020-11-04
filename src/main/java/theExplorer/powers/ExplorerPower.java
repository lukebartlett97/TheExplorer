package theExplorer.powers;

import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ExplorerPower extends AbstractPower {
    private static final Logger logger = LogManager.getLogger(ExplorerPower.class.getName());
    protected String[] descriptions;

    protected void setDescriptions(String[] descriptions) {
        this.descriptions = descriptions;
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
            this.description = descriptions[0] + this.amount + descriptions[1];
        }
    }
}
