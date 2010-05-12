package cg.gl2d.control;

import java.util.List;

import cg.gl2d.model.EditorPoint;

/**
 * Classe agrupadora de rotinas gen�ricas
 */
public class Utils {

	/**
	 * M�todo que normaliza um ponto de um dispositivo e retorna o ponto equivalente no Ortho.
	 * @param a Ponto inicial do dispositivo
	 * @param b Ponto do dispositivo que se deseja normalizar
	 * @param c Ponto final do dispositivo
	 * @param d Ponto inicial do Ortho
	 * @param f Ponto final do Ortho
	 * @return Ponto do Ortho normalizado com rela��o � "b"
	 */
	public static double normalizeE(int a, int b, int c, double d, double f) {
		return ((b - a) * ((f - d) / (double)(c - a))) + d;
	}

	/**
	 * M�todo que normaliza um ponto do Ortho e retorna o ponto equivalente no dispositivo.
	 * @param a Ponto inicial do dispositivo
	 * @param c Ponto final do dispositivo
	 * @param d Ponto inicial do Ortho
	 * @param e Ponto do Ortho que se deseja normalizar
	 * @param f Ponto final do Ortho
	 * @return Ponto do dispositivo normalizado com rela��o � "e"
	 */
	public static int normalizeB(int a, int c, double d, double e, double f) {
		return (int)((e - d) * ((double)(c - a) / (f - d))) + a;
	}
	
	/**
	 * Calcula a dist�ncia entre dois pontos no plano cartesiano em 2D.
	 * @param a Coordenadas (x, y) do ponto inicial
	 * @param b Coordenadas (x, y) do ponto final
	 * @return Dist�ncia entre "a" e "b"
	 */
	public static double distanceBetweenPoints(EditorPoint a, EditorPoint b){
		double x = (b.x - a.x);
		double y = (b.y - a.y);

		return Math.sqrt((x * x) + (y * y));
	}
	
	/**
	 * Retorna o pr�ximo ponto de uma lista circular de pontos.
	 * @param points Lista circular de pontos
	 * @param index �ndice do ponto atual
	 * @return Pr�ximo ponto com rela��o ao �ndice.
	 * Nota: Se (index + 1) >= points.size(), o retorno � o primeiro ponto da lista.
	 */
	public static EditorPoint nextPointInList(List<EditorPoint> points, int index){
		if(index + 1 >= points.size()){
			return points.get(0);
		}
		
		return points.get(index + 1);
	}

}
