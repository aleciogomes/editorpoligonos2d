package cg.gl2d.control;

import java.util.List;

import cg.gl2d.model.EditorPoint;

/**
 * Classe agrupadora de rotinas genéricas
 */
public class Utils {

	/**
	 * Método que normaliza um ponto de um dispositivo e retorna o ponto equivalente no Ortho.
	 * @param a Ponto inicial do dispositivo
	 * @param b Ponto do dispositivo que se deseja normalizar
	 * @param c Ponto final do dispositivo
	 * @param d Ponto inicial do Ortho
	 * @param f Ponto final do Ortho
	 * @return Ponto do Ortho normalizado com relação à "b"
	 */
	public static double normalizeE(int a, int b, int c, double d, double f) {
		return ((b - a) * ((f - d) / (double)(c - a))) + d;
	}

	/**
	 * Método que normaliza um ponto do Ortho e retorna o ponto equivalente no dispositivo.
	 * @param a Ponto inicial do dispositivo
	 * @param c Ponto final do dispositivo
	 * @param d Ponto inicial do Ortho
	 * @param e Ponto do Ortho que se deseja normalizar
	 * @param f Ponto final do Ortho
	 * @return Ponto do dispositivo normalizado com relação à "e"
	 */
	public static int normalizeB(int a, int c, double d, double e, double f) {
		return (int)((e - d) * ((double)(c - a) / (f - d))) + a;
	}
	
	/**
	 * Calcula a distância entre dois pontos no plano cartesiano em 2D.
	 * @param a Coordenadas (x, y) do ponto inicial
	 * @param b Coordenadas (x, y) do ponto final
	 * @return Distância entre "a" e "b"
	 */
	public static double distanceBetweenPoints(EditorPoint a, EditorPoint b){
		double x = (b.x - a.x);
		double y = (b.y - a.y);

		return Math.sqrt((x * x) + (y * y));
	}
	
	/**
	 * Retorna o próximo ponto de uma lista circular de pontos.
	 * @param points Lista circular de pontos
	 * @param index Índice do ponto atual
	 * @return Próximo ponto com relação ao índice.
	 * Nota: Se (index + 1) >= points.size(), o retorno é o primeiro ponto da lista.
	 */
	public static EditorPoint nextPointInList(List<EditorPoint> points, int index){
		if(index + 1 >= points.size()){
			return points.get(0);
		}
		
		return points.get(index + 1);
	}

}
