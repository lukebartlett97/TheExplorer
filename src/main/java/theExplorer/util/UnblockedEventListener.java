package theExplorer.util;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface UnblockedEventListener {
    void unblocked(AbstractPlayer p, AbstractMonster m, int amount);
}
