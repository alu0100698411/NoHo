import java.util.List;

import java.util.ArrayList;

/**
 * @author Gillermo Rivero
 * @author Boris Ballester
 * @author Cristian Luis
 */
public class Mapa {

    //ATRIBUTOS
    private int[][] m_mapaMundi;
    private int[][] m_mapaFeromonas;
    private List<Hormiguero> m_hormigueros;
    private List<Planta> m_plantas;

    //CONSTRUCTOR
    public Mapa(int filas, int columnas, List<Hormiguero> hormigueros, List<Planta> plantas) {
        this.setPlantas(plantas);
        this.setHormigueros(hormigueros);
        this.setMapaFeromonas(new int[filas][columnas]);
        this.setMapaMundi(new int[filas][columnas]);
        this.actualizar();
    }

    //GETTERS
    public int[][] getMapaMundi() {
        return m_mapaMundi;
    }

    public int[][] getMapaFeromonas() {
        return m_mapaFeromonas;
    }

    public List<Hormiguero> getHormigueros() {
        return m_hormigueros;
    }

    public List<Planta> getPlantas() {
        return m_plantas;
    }

    //SETTERS
    public void setMapaMundi(int[][] mapaMundi) {
        this.m_mapaMundi = mapaMundi;
    }

    public void setMapaFeromonas(int[][] mapaFeromonas) {
        this.m_mapaFeromonas = mapaFeromonas;
    }

    public void setHormigueros(List<Hormiguero> hormiguero) {
        this.m_hormigueros = hormiguero;
    }

    public void setPlantas(List<Planta> plantas) {
        this.m_plantas = plantas;
    }

    //METODOS
    public void setPosMapaMundi(Posicion pos, int value) {
        this.m_mapaMundi[pos.x][pos.y] = value;
    }

    public int getPosMapaMundi(Posicion pos) {
        return this.m_mapaMundi[pos.x][pos.y];
    }

    public void setPosMapaFeromonas(Posicion pos, int value) {
        this.m_mapaFeromonas[pos.x][pos.y] = value;
    }

    public int getPosMapaFeromonas(Posicion pos) {
        return this.m_mapaFeromonas[pos.x][pos.y];
    }

    public void actualizar() {
        for (int i = 0; i < this.m_mapaMundi.length; i++) {
            for (int j = 0; j < this.m_mapaMundi[0].length; j++) {
                this.m_mapaMundi[i][j] = 0;
            }
        }
        //Feromonas
        for (int k = 0; k < m_mapaFeromonas.length; k++) {
            for (int j = 0; j < m_mapaFeromonas[k].length; j++) {
                if (getPosMapaFeromonas(new Posicion(k, j)) != 0) {
                    if (getPosMapaFeromonas(new Posicion(k, j)) == Constante.TIEMPO_FEROMONAS){
                        setPosMapaFeromonas(new Posicion(k, j), 0);
                    } else {
                    setPosMapaFeromonas(new Posicion(k, j), getPosMapaFeromonas(new Posicion(k, j)) + 1);}
                }
            }
        }

        //Mapa de agentes
        for (int i = 0; i < this.m_plantas.size(); i++) {
            Posicion posicionPlanta = this.m_plantas.get(i).getPos();
            setPosMapaMundi(posicionPlanta, 3);
        }
        for (int i = 0; i < this.m_hormigueros.size(); i++) {
            List<Hormiga> hormigasHormiguero = this.m_hormigueros.get(i).getListaHormigas();
            for (int j = 0; j < hormigasHormiguero.size(); j++) {
                Hormiga hormigaAux = hormigasHormiguero.get(j);
                Posicion posicionHormiga = hormigaAux.getPos();
                int valorCelda = getPosMapaMundi(posicionHormiga);
                switch (valorCelda) {
                    case 0:
                        setPosMapaMundi(posicionHormiga, 2);
                        break;
                    case 3:
                        setPosMapaMundi(posicionHormiga, 4);
                        break;
                }
                if (hormigaAux.isComida()) {
                    setPosMapaFeromonas(posicionHormiga, 1);
                }
            }
        }
    }

    public int[][] obtenerAdyacencia(Posicion pos) {
        int[][] matrizAdyacencia = new int[3][3];

        int filas = this.m_mapaMundi.length;
        int columnas = this.m_mapaMundi[0].length;

        //NORTE
        if (pos.x - 1 < 0) {
            matrizAdyacencia[0][1] = 1;//BLOQUEADA
        } else {
            matrizAdyacencia[0][1] = getPosMapaMundi(new Posicion(pos.x - 1, pos.y));
        }
        //SUR
        if (pos.x + 1 >= filas) {
            matrizAdyacencia[2][1] = 1;//BLOQUEADA
        } else {
            matrizAdyacencia[2][1] = getPosMapaMundi(new Posicion(pos.x + 1, pos.y));
        }
        //ESTE
        if (pos.y + 1 >= columnas) {
            matrizAdyacencia[1][2] = 1;//BLOQUEADA
        } else {
            matrizAdyacencia[1][2] = getPosMapaMundi(new Posicion(pos.x, pos.y + 1));
        }
        //OESTE
        if (pos.y - 1 < 0) {
            matrizAdyacencia[1][0] = 1;//BLOQUEADA
        } else {
            matrizAdyacencia[1][0] = getPosMapaMundi(new Posicion(pos.x, pos.y - 1));
        }
        //NOROESTE 
        if (pos.x - 1 < 0 || pos.y - 1 < 0) {
            matrizAdyacencia[0][0] = 1;//BLOQUEADA
        } else {
            matrizAdyacencia[0][0] = getPosMapaMundi(new Posicion(pos.x - 1, pos.y - 1));
        }
        //NORESTE
        if (pos.x - 1 < 0 || pos.y + 1 >= columnas) {
            matrizAdyacencia[0][2] = 1;//BLOQUEADA
        } else {
            matrizAdyacencia[0][2] = getPosMapaMundi(new Posicion(pos.x - 1, pos.y + 1));
        }
        //SUROESTE
        if (pos.x + 1 >= filas || pos.y - 1 < 0) {
            matrizAdyacencia[2][0] = 1;//BLOQUEADA
        } else {
            matrizAdyacencia[2][0] = getPosMapaMundi(new Posicion(pos.x + 1, pos.y - 1));
        }
        //SURESTE
        if (pos.x + 1 >= filas || pos.y + 1 >= columnas) {
            matrizAdyacencia[2][2] = 1;//BLOQUEADA
        } else {
            matrizAdyacencia[2][2] = getPosMapaMundi(new Posicion(pos.x + 1, pos.y + 1));
        }
        //CENTRO
        matrizAdyacencia[1][1] = getPosMapaMundi(pos);
        return matrizAdyacencia;
    }

    public int[][] obtenerFeromonas(Posicion pos) {
        int[][] matrizAdyacencia = new int[3][3];

        int filas = this.m_mapaMundi.length;
        int columnas = this.m_mapaMundi[0].length;

        //NORTE
        if (pos.x - 1 < 0) {
            matrizAdyacencia[0][1] = 1;//BLOQUEADA
        } else {
            matrizAdyacencia[0][1] = getPosMapaFeromonas(new Posicion(pos.x - 1, pos.y));
        }
        //SUR
        if (pos.x + 1 >= filas) {
            matrizAdyacencia[2][1] = 1;//BLOQUEADA
        } else {
            matrizAdyacencia[2][1] = getPosMapaFeromonas(new Posicion(pos.x + 1, pos.y));
        }
        //ESTE
        if (pos.y + 1 >= columnas) {
            matrizAdyacencia[1][2] = 1;//BLOQUEADA
        } else {
            matrizAdyacencia[1][2] = getPosMapaFeromonas(new Posicion(pos.x, pos.y + 1));
        }
        //OESTE
        if (pos.y - 1 < 0) {
            matrizAdyacencia[1][0] = 1;//BLOQUEADA
        } else {
            matrizAdyacencia[1][0] = getPosMapaFeromonas(new Posicion(pos.x, pos.y - 1));
        }
        //NOROESTE 
        if (pos.x - 1 < 0 || pos.y - 1 < 0) {
            matrizAdyacencia[0][0] = 1;//BLOQUEADA
        } else {
            matrizAdyacencia[0][0] = getPosMapaFeromonas(new Posicion(pos.x - 1, pos.y - 1));
        }
        //NORESTE
        if (pos.x - 1 < 0 || pos.y + 1 >= columnas) {
            matrizAdyacencia[0][2] = 1;//BLOQUEADA
        } else {
            matrizAdyacencia[0][2] = getPosMapaFeromonas(new Posicion(pos.x - 1, pos.y + 1));
        }
        //SUROESTE
        if (pos.x + 1 >= filas || pos.y - 1 < 0) {
            matrizAdyacencia[2][0] = 1;//BLOQUEADA
        } else {
            matrizAdyacencia[2][0] = getPosMapaFeromonas(new Posicion(pos.x + 1, pos.y - 1));
        }
        //SURESTE
        if (pos.x + 1 >= filas || pos.y + 1 >= columnas) {
            matrizAdyacencia[2][2] = 1;//BLOQUEADA
        } else {
            matrizAdyacencia[2][2] = getPosMapaFeromonas(new Posicion(pos.x + 1, pos.y + 1));
        }
        //CENTRO
        matrizAdyacencia[1][1] = getPosMapaFeromonas(pos);
        return matrizAdyacencia;
    }

    public void avanzar() {
        for (int i = 0; i < this.m_hormigueros.size(); i++) {
            Hormiguero hormigueroAux = this.m_hormigueros.get(i);
            List<Hormiga> hormigasHormiguero = hormigueroAux.getListaHormigas();
            for (int j = 0; j < hormigasHormiguero.size(); j++) {
                Hormiga hormigaAux = hormigasHormiguero.get(j);
                boolean borrado = hormigaAux.isComida();
                hormigaAux.mover(obtenerAdyacencia(hormigaAux.getPos()), obtenerFeromonas(hormigaAux.getPos()), hormigueroAux.getPos());
                boolean confirmar = hormigaAux.isComida();
                if ((borrado == false) && confirmar) {
                    //System.out.println("Alimento encontrado");
                    if (!eliminarPlanta(hormigaAux.getPos())) {
                        hormigaAux.setComida(false);
                        //System.err.println("Error 1");
                    }
                }
                if (!hormigaAux.isAlive()) {
                    hormigasHormiguero.remove(hormigaAux);
                }
            }
        }
        int max = this.m_plantas.size();
        if (max > 1000){
            max = 1000;
        }
        for (int i = 0; i < max; i++) {
            Planta plantaAux = this.m_plantas.get(i);
            if (plantaAux.getContador() > Constante.TIEMPO_REPRODUCCION) {
                this.m_plantas.add(new Planta(1, 5, generarPosicion(plantaAux.getPos()), "spora"));
                plantaAux.setContador(0);
            } else {
                plantaAux.setContador(plantaAux.getContador() + 1);
            }
        }
    }

    private boolean eliminarPlanta(Posicion pos) {
        for (int i = 0; i < this.m_plantas.size(); i++) {
            if (m_plantas.get(i).getPos().equal(pos)) {
                this.m_plantas.remove(i);
                return true;
            }
        }
        return false;
    }

    public Posicion generarPosicion() {
        int x = (int) Math.round(Math.random() * this.m_mapaMundi.length);
        int y = (int) Math.round(Math.random() * this.m_mapaMundi[0].length);
        if (x >= Constante.MAX_MAPA_X) {
            x = Constante.MAX_MAPA_X - 1;
        }
        if (y >= Constante.MAX_MAPA_Y) {
            y = Constante.MAX_MAPA_Y - 1;
        }
        return new Posicion(x, y);
    }

    public boolean comprobarPosicion(Posicion pos) {
        if ((pos.getX() >= Constante.MAX_MAPA_X) || (pos.getX() < 0)) {
            return false;
        }
        if ((pos.getY() >= Constante.MAX_MAPA_Y) || (pos.getY() < 0)) {
            return false;
        }
        return true;
    }

    private Posicion generarPosicion(Posicion pos) {
        int x, y;
        Posicion nueva;
        do {
            x = pos.getX();
            y = pos.getY();
            double alteracion = Math.random();
            if (alteracion > 0.75) {
                x++;
            } else {
                if (alteracion < 0.25) {
                    x--;
                }
            }
            alteracion = Math.random();
            if (alteracion > 0.75) {
                y++;
            } else {
                if (alteracion < 0.25) {
                    y--;
                }
            }
            nueva = new Posicion (x, y);
        } while (!comprobarPosicion(nueva));
        return nueva;
    }

}
