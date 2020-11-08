package theExplorer.companions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import theExplorer.ExplorerMod;

public class CompanionCreature extends AbstractCreature {
    private Texture img;
    private String actMessage;

    public CompanionCreature(Texture img, String actMessage) {
        this.img = img;
        this.actMessage = actMessage;
        this.drawX = (float)Settings.WIDTH * 0.35F;
        this.drawY = AbstractDungeon.floorY;
        this.isPlayer = false;
        this.hb_h = 100 * Settings.scale;
        this.hb_w = 50 * Settings.scale;
        this.hb_x = 40 * Settings.scale;
        this.hb_y = -10 * Settings.scale;
        this.dialogX = 0;
        this.dialogY = 0;
        this.hb = new Hitbox(this.hb_w, this.hb_h);
        this.refreshHitboxLocation();
    }

    @Override
    public void damage(DamageInfo damageInfo) {
        //Do Nothing, we don't hurt cute companions around these parts.
    }

    @Override
    public void render(SpriteBatch sb) {
        if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getCurrRoom() instanceof MonsterRoom) && !AbstractDungeon.player.isDead) {
            renderCompanionImage(sb);
        }
    }

    public void act() {
        AbstractDungeon.effectList.add(new CompanionSpeechBubble(this.hb.cX + this.dialogX, this.hb.cY + this.dialogY, 2.5F, actMessage, true));
    }

    @Override
    protected void refreshHitboxLocation() {
        this.hb.move(this.drawX + this.hb_x + this.animX, this.drawY + this.hb_y + this.hb_h / 2.0F);
    }

    public void renderCompanionImage(SpriteBatch sb) {
        if (this.atlas != null) {
            this.state.update(Gdx.graphics.getDeltaTime());
            this.state.apply(this.skeleton);
            this.skeleton.updateWorldTransform();
            this.skeleton.setPosition(this.drawX + this.animX, this.drawY + this.animY);
            this.skeleton.setColor(this.tint.color);
            this.skeleton.setFlip(this.flipHorizontal, this.flipVertical);
            sb.end();
            CardCrawlGame.psb.begin();
            sr.draw(CardCrawlGame.psb, this.skeleton);
            CardCrawlGame.psb.end();
            sb.begin();
        } else {
            sb.setColor(Color.WHITE);
            sb.draw(this.img, this.drawX - (float)this.img.getWidth() * Settings.scale / 2.0F + this.animX, this.drawY, (float)this.img.getWidth() * Settings.scale, (float)this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
        }
        this.hb.render(sb);
    }
}
