package cg.gl2d.model;

// esta classe está relativamente genérica, quem sabe um dia espcializar.

public class Transform {

	public static final int Lx = 12;
	public static final int Ly = 13;

	public static final int Sx = 0;
	public static final int Sy = 5;

	private double[] matrix = { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 };

	public Transform() {
	}

	public EditorPoint transformPoint(EditorPoint point) {
		EditorPoint pointResult = new EditorPoint(matrix[0] * point.x + matrix[4] * point.y + matrix[8] * 0 + matrix[12] * 1.0, matrix[1] * point.x + matrix[5] * point.y + matrix[9] * 0 + matrix[13] * 1.0);
		return pointResult;
	}

	public Transform transformMatrix(Transform t) {
		Transform result = new Transform();
		for (int i = 0; i < 16; ++i)
			result.matrix[i] = matrix[i % 4] * t.matrix[i / 4 * 4] + matrix[(i % 4) + 4] * t.matrix[i / 4 * 4 + 1] + matrix[(i % 4) + 8] * t.matrix[i / 4 * 4 + 2] + matrix[(i % 4) + 12] * t.matrix[i / 4 * 4 + 3];
		return result;
	}

	public void setElement(int index, double value) {
		matrix[index] = value;
	}
	
	public void setAngle(double angulo){
		double cos = Math.cos(angulo);
		double sen = Math.sin(angulo);
		
		matrix[0] = cos;
		matrix[1] = -sen;
		matrix[4] = sen;
		matrix[5] = cos;
	}
}
