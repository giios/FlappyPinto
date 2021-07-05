package com.cursoandroid.flappypinto;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.Random;

public class FlappyPinto extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture[] pinto;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
	private Random numeroRandomico;
	private BitmapFont fonte;
	private Circle pintoCirculo;
	private Rectangle retanguloCanoTopo;
	private Rectangle retanguloCanoBaixo;
	private ShapeRenderer shape;


	//Atributos de configuração
	private int larguraDispositivo;
	private int alturaDispositivo;
	private int estadoJogo = 0; // 0-> jogo não iniciado  |  1-> jogo iniciado
	private int pontuacao = 0;

	private float variacao = 0;
	private float velocidadeQueda=0;
	private float posicaoInicialVertical;
	private float posicaoMovimentoCanoHorizontal;
	private float espacoEntreCanos;
	private float deltaTime;
	private float alturaEntreCanosRandomica;
	private boolean marcouPonto = false;


	@Override
	public void create () {
		
		batch = new SpriteBatch();
		numeroRandomico = new Random();
		pintoCirculo = new Circle();
		retanguloCanoBaixo = new Rectangle();
		retanguloCanoTopo = new Rectangle();
		shape = new ShapeRenderer();
		fonte = new BitmapFont();
		fonte.setColor(Color.WHITE);
		fonte.getData().setScale(5);

		pinto = new Texture[3];
		pinto[0] = new Texture("passaro1.png");
		pinto[1] = new Texture("passaro2.png");
		pinto[2] = new Texture("passaro3.png");
		fundo = new Texture("fundo.png");
		canoBaixo = new Texture("cano_baixo_maior.png");
		canoTopo = new Texture("cano_topo_maior.png");

		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		posicaoInicialVertical = alturaDispositivo / 2;
		posicaoMovimentoCanoHorizontal = larguraDispositivo - 100;
		espacoEntreCanos = 350;

	}

	@Override
	public void render () {

		deltaTime = Gdx.graphics.getDeltaTime();
		variacao += deltaTime * 10;  //velocidade da animação
		//Gdx.app.log("Variacao", "Variacao" + Gdx.graphics.getDeltaTime()); // apresenta o log com msg
		//variacao += 0.1; //deixando mais lento o processo de incremento
		if (variacao > 2) {
			variacao = 0;
		}

		if( estadoJogo == 0){ //Não iniciado

			if (Gdx.input.justTouched()){
				estadoJogo = 1;

			}

		}else {


			deltaTime = Gdx.graphics.getDeltaTime();
			variacao += deltaTime * 10;  //velocidade da animação
			//Gdx.app.log("Variacao", "Variacao" + Gdx.graphics.getDeltaTime()); // apresenta o log com msg
			//variacao += 0.1; //deixando mais lento o processo de incremento
			posicaoMovimentoCanoHorizontal -= deltaTime * 200;
			velocidadeQueda++;
			if (variacao > 2) {
				variacao = 0;
			}

			//metodo p verificar toque na tela
			if (Gdx.input.justTouched()) {
				//velocidade do pulo, quanto maior, mais alto  ele sobe
				velocidadeQueda = -15;
			}
			;
			//configura reinicio da entrada dos canos
			if (posicaoMovimentoCanoHorizontal < -canoTopo.getWidth()) {
				posicaoMovimentoCanoHorizontal = larguraDispositivo;
				alturaEntreCanosRandomica = numeroRandomico.nextInt(1000) - 600;
				marcouPonto = false;

			}

			//configura a queda
			if (posicaoInicialVertical >= 0 || velocidadeQueda < 0) {
				posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;
			}
			//verifica pontuação
			if(posicaoMovimentoCanoHorizontal < 120){
				if(!marcouPonto){
					pontuacao++;
					marcouPonto = true;
				}


			}
		}
		batch.begin();
		batch.draw(fundo, 0,0, larguraDispositivo, alturaDispositivo);
		batch.draw(canoTopo, posicaoMovimentoCanoHorizontal, alturaDispositivo - canoTopo.getHeight() + espacoEntreCanos / 10 + alturaEntreCanosRandomica);
		batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica);
		batch.draw(pinto[ (int)variacao], 130, posicaoInicialVertical);
		fonte.draw(batch, String.valueOf(pontuacao), larguraDispositivo / 2, alturaDispositivo - 50);
		batch.end();

		//circulo do pinto
		pintoCirculo.set(130 + pinto[0].getWidth() / 2 , posicaoInicialVertical + pinto[0].getHeight() / 2, pinto[0].getWidth() / 2);
		retanguloCanoBaixo = new Rectangle(
				posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica,
				canoBaixo.getWidth(), canoBaixo.getHeight()
		);

		retanguloCanoTopo = new Rectangle(
				posicaoMovimentoCanoHorizontal, alturaDispositivo - canoTopo.getHeight() + espacoEntreCanos / 10 + alturaEntreCanosRandomica,
				canoTopo.getWidth(), canoTopo.getHeight()
		);

		//Desenhar formas
		/*shape.begin(ShapeRenderer.ShapeType.Filled);
		shape.circle(pintoCirculo.x, pintoCirculo.y, pintoCirculo.radius);
		shape.rect(retanguloCanoBaixo.x, retanguloCanoBaixo.y, retanguloCanoBaixo.width, retanguloCanoBaixo.height);
		shape.rect(retanguloCanoTopo.x, retanguloCanoTopo.y, retanguloCanoTopo.width, retanguloCanoTopo.height);
		shape.setColor(Color.RED);
		shape.end();

		 */


	}
	
	@Override
	public void dispose () {

	}
}
