package theExplorer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theExplorer.ExplorerMod;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

public class SpawnMonsterAction extends AbstractGameAction {

    private static final float DURATION = 0.1F;
    private AbstractMonster m;
    private int targetSlot;

    public SpawnMonsterAction(float x, float y, Class<? extends AbstractCreature> monsterClass) {
        this(x, y, -99, monsterClass);
    }

    public SpawnMonsterAction(float x, float y, int slot, Class<? extends AbstractCreature> monsterClass) {
        this.actionType = ActionType.SPECIAL;
        this.duration = DURATION;
        try {
            try {
                this.m = (AbstractMonster) monsterClass.getDeclaredConstructor(float.class, float.class).newInstance(x, y);
                this.m.drawX = x;
                this.m.drawY = y;
            } catch (NoSuchMethodException e) {
                try {
                    this.m = (AbstractMonster) monsterClass.getDeclaredConstructor().newInstance();
                    this.m.drawX = x;
                    this.m.drawY = y;
                }catch (NoSuchMethodException f) {
                    this.m = (AbstractMonster) monsterClass.getDeclaredConstructor(float.class, float.class, int.class).newInstance(x, y, 0);
                    this.m.drawX = x;
                    this.m.drawY = y;
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            ExplorerMod.logger.error("Couldn't create monster of class: " + monsterClass.getSimpleName() + ". Guess you got lucky!!!");
            e.printStackTrace();
            return;
        }

        this.targetSlot = slot;
        if (AbstractDungeon.player.hasRelic("Philosopher's Stone")) {
            m.addPower(new StrengthPower(m, 1));
            AbstractDungeon.onModifyPower();
        }

    }

    public void update() {
        if(m != null) {
            if(targetSlot < 0) {
                targetSlot = getPos();
            }
            AbstractDungeon.getCurrRoom().monsters.addMonster(targetSlot, m);
            this.m.init();
            this.m.applyPowers();
            this.m.showHealthBar();
            if (ModHelper.isModEnabled("Lethality")) {
                this.addToBot(new ApplyPowerAction(this.m, this.m, new StrengthPower(this.m, 3), 3));
            }

            if (ModHelper.isModEnabled("Time Dilation")) {
                this.addToBot(new ApplyPowerAction(this.m, this.m, new SlowPower(this.m, 0)));
            }
        }
        this.isDone = true;
        this.tickDuration();
    }

    private int getPos() {
        int position = 0;
        Iterator var5 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while(var5.hasNext()) {
            AbstractMonster mo = (AbstractMonster)var5.next();
            if (this.m.drawX > mo.drawX) {
                ++position;
            }
        }
        return position;
    }
}
