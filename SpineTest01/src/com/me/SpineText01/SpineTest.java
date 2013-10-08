package com.me.SpineText01;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
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
import com.esotericsoftware.spine.SkeletonRenderer;

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
		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		
		ini();
	}

	private void ini() {
		atlas = new TextureAtlas(Gdx.files.internal("data/spine/spineboy.atlas"));
		sb = new SkeletonBinary(atlas);
		sd = sb.readSkeletonData(Gdx.files
                .internal("data/spine/spineboy.skel"));
		
		skeleton = new Skeleton(sd);
		walkAnimation = sd.findAnimation("walk");
		animationTime = 0;
		renderer = new SkeletonRenderer();
		
		root = skeleton.getRootBone();
		root.setX(100);
		root.setY(20);
		
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
		
//		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		delta = Gdx.graphics.getDeltaTime();
		lastTime = animationTime;
		animationTime += delta;
//		walkAnimation.apply(skeleton, animationTime, true); // true is for loop
		walkAnimation.apply(skeleton, lastTime, animationTime, true, null);
		skeleton.updateWorldTransform();
        skeleton.update(delta);
//		renderSkeleton(skeleton);
		
        sprite.setX(200);sprite.setY(200);
		sprite.draw(batch);
		renderer.draw(batch, skeleton);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		batch.setProjectionMatrix(camera.combined);
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
//		renderer.setProjectionMatrix(batch.getProjectionMatrix());
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
