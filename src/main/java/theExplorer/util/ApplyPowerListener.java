package theExplorer.util;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public interface ApplyPowerListener {

    public void onApplyPower(AbstractPower power, AbstractCreature target);

}
