package theExplorer.util;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public interface ApplyPowerListener {

    void onApplyPower(AbstractPower power, AbstractCreature target);

}
