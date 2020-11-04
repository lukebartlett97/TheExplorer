package theExplorer.monsters;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import theExplorer.ExplorerMod;
import theExplorer.powers.VenomPower;
import theExplorer.util.AscensionHelper;
import theExplorer.util.MovePicker;

public class Snek extends AbstractMonster {
    public static final String ID = ExplorerMod.makeID(Snek.class.getSimpleName());
    public static final String ENCOUNTER_NAME = ID;
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    // location
    private static final float HB_X = 0.0f;
    private static final float HB_Y = -15.0f;
    private static final float HB_W = 200.0F;
    private static final float HB_H = 100.0f;
    // stats
    private static final int HP_MIN = 30;
    private static final int HP_MAX = 35;
    private static final int HP_MIN_A = HP_MIN + 5;
    private static final int HP_MAX_A = HP_MAX + 5;
    private static final int VENOMOUS_BITE_DMG = 6;
    private static final int VENOMOUS_BITE_DMG_A = 8;
    private static final int VENOMOUS_BITE_VENOM = 1;
    private static final int VENOMOUS_BITE_VENOM_A = 2;
    private static final int BITE_DMG = 8;
    private static final int BITE_DMG_A = 10;
    private static final int GLARE_WEAK = 2;
    private static final int GLARE_WEAK_A = 3;
    private int venomousBiteVenom;
    private int glareWeak;

    // moves
    private static final byte VENOMOUS_BITE = 1;
    private static final byte BITE = 2;
    private static final byte GLARE = 3;

    public Snek(float offsetX, float offsetY) {
        super(NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, null, offsetX, offsetY);
        if (AscensionHelper.tougher(this.type)) {
            this.setHp(HP_MIN_A, HP_MAX_A);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }
        int biteDmg = AscensionHelper.deadlier(this.type) ? BITE_DMG_A : BITE_DMG;
        int venomousBiteDmg = AscensionHelper.deadlier(this.type) ? VENOMOUS_BITE_DMG_A : VENOMOUS_BITE_DMG;
        this.venomousBiteVenom = AscensionHelper.harder(this.type) ? VENOMOUS_BITE_VENOM_A : VENOMOUS_BITE_VENOM;
        this.glareWeak = AscensionHelper.harder(this.type) ? GLARE_WEAK_A : GLARE_WEAK;
        this.damage.add(new DamageInfo(this, venomousBiteDmg));
        this.damage.add(new DamageInfo(this, biteDmg));
        this.loadAnimation("images/monsters/theBottom/slimeS/skeleton.atlas", "images/monsters/theBottom/slimeS/skeleton.json", 1.0F);
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case VENOMOUS_BITE: {
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new VenomPower(AbstractDungeon.player, AbstractDungeon.player, venomousBiteVenom)));
                break;
            }
            case BITE: {
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
                break;
            }
            case GLARE: {
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, glareWeak, true)));
                break;
            }
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        MovePicker moves = new MovePicker();
        if (!this.lastMove(VENOMOUS_BITE))
            moves.add(MOVES[0], VENOMOUS_BITE, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 1.0F);
        if (!this.lastMove(BITE))
            moves.add(MOVES[1], BITE, Intent.ATTACK, this.damage.get(1).base, 1.0F);
        if (!this.lastMove(GLARE))
            moves.add(MOVES[2], GLARE, Intent.DEBUFF, 1.0F);
        moves.pickRandomMove(this);
    }
}
