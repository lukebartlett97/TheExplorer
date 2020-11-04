package theExplorer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import theExplorer.powers.CompanionPower;
import theExplorer.util.CompanionService;

public class CompanionActAction extends AbstractGameAction {

    public CompanionActAction() {
    }

    @Override
    public void update() {
        CompanionPower companionPower = CompanionService.getCompanionPower();
        if (companionPower != null) {
            companionPower.act();
        }
        this.isDone = true;
    }
}
