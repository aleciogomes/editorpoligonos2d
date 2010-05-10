package cg.gl2d.control;

import java.util.List;

import cg.gl2d.model.EditorPoint;
import cg.gl2d.model.Polygon;
import cg.gl2d.model.Shape;

public class Scanline {

	private Shape selectedShape;

	public Scanline() {
	}

	public int scan(List<Shape> shapes, EditorPoint clickedPoint) {
		for (Shape s : shapes) {
			int interseccoes = 0;

			if (Polygon.class.isAssignableFrom(s.getClass())) {
				Polygon p = (Polygon) s;
				if (p.getPoints().size() > 1) {
					for (int i = 0; i < p.getPoints().size(); i++) {
						EditorPoint p1 = p.getPoints().get(i);
						EditorPoint p2 = Utils
								.nextPointInList(p.getPoints(), i);

						if (p1.y != p2.y) {
							double ti = (clickedPoint.y - p1.y) / (p2.y - p1.y);

							if (ti > 0.0 && ti < 1.0) {
								double xi = p1.x + ((p2.x - p1.x) * ti);

								if (xi > clickedPoint.x) {
									interseccoes++;
								} else if (xi == clickedPoint.x) {
									interseccoes = 1;
									break;
								}
							}
						} else {
							// TODO
						}
					}
				}
			}

			if ((interseccoes % 2) == 1) {
				selectedShape = s;
				return interseccoes;
			}
		}

		selectedShape = null;
		return 0;
	}

	public Shape getSelectedShape() {
		return selectedShape;
	}

}
