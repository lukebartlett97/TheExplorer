package theExplorer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExplorer.util.UnblockedEventListener;

public class UnblockedAction extends AbstractGameAction {
    AbstractPlayer player;
    AbstractMonster monster;
    int startHealth;
    UnblockedEventListener listener;

    public UnblockedAction(final AbstractPlayer p, final AbstractMonster m, UnblockedEventListener listener) {
        player = p;
        monster = m;
        startHealth = m.currentHealth;
        this.listener = listener;
    }


    @Override
    public void update() {
        int amount = startHealth - monster.currentHealth;
        if (amount > 0) {
            listener.unblocked(player, monster, amount);
        }
        this.isDone = true;
    }
}
