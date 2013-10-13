package com.me.SpineText01;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.Slot;

public class SpineTest implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	private float animationTime;
	private Skeleton skeleton;
	private Animation walkAnimation;
	private TextureAtlas atlas;
	private SkeletonBinary sb;
	private SkeletonData sd;
	private float delta;
	private float lastTime;
	private com.esotericsoftware.spine.SkeletonRenderer renderer;
	private Bone root;
	
	public int VIRTUAL_WIDTH = 800;
	public int VIRTUAL_HEIGHT = 480;
	private Animation winkAnimation;
	private float winkTime;
	private float lastWinkTime;
	private Slot slot;
	private Color color;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();
		
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
		
		sprite = new Sprite(region);
//		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
//		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
//		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		
		ini();
	}

	private void ini() {
		// "data/spine/spineboy.atlas"
		// "data/spine/spineboy.skel"

		// "data/spine/ball/skeleton.atlas"
		// "data/spine/ball/skeleton.skel"
		String atlasfile =  "data/spine/ball/skeleton.atlas";
		String skfile = "data/spine/ball/skeleton.skel";
		String jsonfile = "data/spine/ball/skeleton.json";
		
		atlas = new TextureAtlas(Gdx.files.internal(atlasfile));
		sb = new SkeletonBinary(atlas);
//		sd = sb.readSkeletonData(Gdx.files
//                .internal(skfile));
		SkeletonJson json = new SkeletonJson(atlas);
		sd = json.readSkeletonData(Gdx.files.internal(jsonfile)); //.readSkeletonData("mySkeleton.json");
		
		skeleton = new Skeleton(sd);
		skeleton.setSkin("eyes01");
//		walkAnimation = sd.findAnimation("walk");
		walkAnimation = sd.findAnimation("stand01");
		winkAnimation = sd.findAnimation("wink");
		animationTime = 0;
		winkTime = 0;
		renderer = new SkeletonRenderer();
		
		root = skeleton.getRootBone();
		root.setX(20);
		root.setY(20);
		
		log("before color: " + color);
		slot = skeleton.findSlot("ballbase01");
		color = slot.getColor();
		color.set(1, 0, 0, 1);
		color.r = 1f;color.g = 0f;color.b = 1f;
		log("after color: " + color);
		
		skeleton.updateWorldTransform();
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		delta = Gdx.graphics.getDeltaTime();
		lastTime = animationTime;
		animationTime += delta;
		
		if (winkTime > 1.5) {
			log("A winkTime: " + winkTime + " last: " + lastWinkTime + " delta: " + delta);
			log("color: " + color);
			winkTime = 0;
		}
		lastWinkTime = winkTime;
		winkTime += delta;
		
//		log("B winkTime: " + winkTime + " last: " + lastWinkTime + " delta: " + delta);
//		root.setX(((root.getX() + 230 * delta)%(VIRTUAL_WIDTH*1.2f)));
		root.setX((230 * animationTime)%(VIRTUAL_WIDTH + 400) - 200);
//		walkAnimation.apply(skeleton, animationTime, true); // true is for loop
		walkAnimation.apply(skeleton, lastTime, animationTime, true, null);
		winkAnimation.apply(skeleton, lastWinkTime*10, winkTime*10, false, null);
//		winkAnimation.apply(skeleton, lastTime*10, animationTime*10, true, null);
		winkAnimation.getDuration();
		skeleton.updateWorldTransform();
        skeleton.update(delta);
//		renderSkeleton(skeleton);
		
//        sprite.setPosition(-40, -40);
//        sprite.setScale(10);
		sprite.draw(batch);
		renderer.draw(batch, skeleton);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		
		batch.setProjectionMatrix(camera.combined);
//		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
//		renderer.setProjectionMatrix(batch.getProjectionMatrix());
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	/**Logs text to Gdx.app.log()
	 * @param text
	 */
	private void log(String text) {
		Gdx.app.log("gdxtest", text);
	}
}
