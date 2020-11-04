package theExplorer.monsters;

import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theExplorer.ExplorerMod;
import theExplorer.actions.ApplyStrengthToAllEnemiesAction;
import theExplorer.util.AscensionHelper;

public class Box extends AbstractMonster {
    public static final String ID = ExplorerMod.makeID(Box.class.getSimpleName());
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
    private static final int HP_MAX = 35;
    private static final int HP_MAX_A = HP_MAX + 5;
    private static final int ENRAGE_STR = 1;
    private static final int ENRAGE_STR_A = 1;
    private int enrageStr;
    private boolean deathrattle = true;


    private static final byte ENRAGE = 1;

    private static final byte FROGS = 1;
    private static final byte SNEKS = 2;
    private static final byte SCORPYS = 3;
    private byte enemy;
    private int currentStrengthBuff;

    // moves

    public Box(float offsetX, float offsetY) {
        super(NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, null, offsetX, offsetY);
        if (AscensionHelper.tougher(this.type)) {
            this.setHp(HP_MAX_A);
        }
        this.enrageStr = AscensionHelper.deadlier(this.type) ? ENRAGE_STR_A : ENRAGE_STR;
        this.loadAnimation("images/monsters/theBottom/slimeS/skeleton.atlas", "images/monsters/theBottom/slimeS/skeleton.json", 1.0F);
    }

    @Override
    public void takeTurn() {
        addToBot(new ApplyPowerAction(this, this, new StrengthPower(AbstractDungeon.player, enrageStr)));
        addToBot(new RollMoveAction(this));
    }

    private void rollSpawner() {
        byte[] enemies;
        if (enemy == FROGS)
            enemies = new byte[]{SNEKS, SCORPYS};
        else if (enemy == SNEKS)
            enemies = new byte[]{FROGS, SCORPYS};
        else if (enemy == SCORPYS)
            enemies = new byte[]{FROGS, SNEKS};
        else
            enemies = new byte[]{FROGS, SNEKS, SCORPYS};
        float r = AbstractDungeon.aiRng.random(enemies.length - 1);
        enemy = enemies[(int) r];
        animalShout();
    }

    private void animalShout() {
        switch (enemy) {
            case FROGS: {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 1.0F, 2.0F));
                break;
            }
            case SNEKS: {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 1.0F, 2.0F));
                break;
            }
            case SCORPYS: {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[2], 1.0F, 2.0F));
                break;
            }
        }
    }

    @Override
    public void healthBarUpdatedEvent() {

        this.currentStrengthBuff = (this.hasPower(StrengthPower.POWER_ID) && this.getPower(StrengthPower.POWER_ID).amount > 0) ? this.getPower(StrengthPower.POWER_ID).amount : 0;
        if (currentHealth < 1 && deathrattle) {
            deathrattle = false;
            currentHealth = 1;
            suicide();
        }
        super.healthBarUpdatedEvent();
    }

    @Override
    protected void getMove(int i) {
        setMove(MOVES[0], ENRAGE, Intent.BUFF);
        rollSpawner();
    }

    public void suicide() {
        addToBot(new CannotLoseAction());
        addToBot(new AnimateShakeAction(this, 1.0F, 0.1F));
        addToBot(new HideHealthBarAction(this));
        switch (enemy) {
            case FROGS: {
                addToBot(new SpawnMonsterAction(new Frog(-300F, 0), false));
                addToBot(new SpawnMonsterAction(new Frog(0F, 0), false));
                addToBot(new SpawnMonsterAction(new Frog(300F, 0), false));
                break;
            }
            case SNEKS: {
                addToBot(new SpawnMonsterAction(new Snek(-300F, 0), false));
                addToBot(new SpawnMonsterAction(new Snek(0F, 0), false));
                addToBot(new SpawnMonsterAction(new Snek(300F, 0), false));
                break;
            }
            case SCORPYS: {
                addToBot(new SpawnMonsterAction(new Scorpy(-300F, 0), false));
                addToBot(new SpawnMonsterAction(new Scorpy(0F, 0), false));
                addToBot(new SpawnMonsterAction(new Scorpy(300F, 0), false));
                break;
            }
        }
        addToBot(new SuicideAction(this, false));
        addToBot(new CanLoseAction());
        addToBot(new PressEndTurnButtonAction());
        if (currentStrengthBuff > 0) {
            addToBot(new ApplyStrengthToAllEnemiesAction(currentStrengthBuff));
        }
    }
}
