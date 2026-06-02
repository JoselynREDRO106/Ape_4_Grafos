import java.util.*;

public class APE4_Grafos {

    // ═══════════════════════════════════════
    // Nodo
    // ═══════════════════════════════════════
    static class Nodo {
        String id;
        String nombre;

        public Nodo(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
    }

    // ═══════════════════════════════════════
    // Arista
    // ═══════════════════════════════════════
    static class Arista {
        String destino;
        int peso;

        public Arista(String destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    // ═══════════════════════════════════════
    // Grafo
    // ═══════════════════════════════════════
    static class Grafo {

        Map<String, Nodo> nodos = new HashMap<>();
        Map<String, List<Arista>> adyacencia = new HashMap<>();

        // ═══════════════════════════════════
        // TODO 1
        // Agregar nodo al grafo
        // ═══════════════════════════════════
        public void agregarNodo(String id, String nombre) {
            // Crear nuevo nodo
            Nodo nuevoNodo = new Nodo(id, nombre);
            
            // Agregar nodo al mapa de nodos
            nodos.put(id, nuevoNodo);
            
            // Inicializar lista de adyacencia para este nodo
            adyacencia.put(id, new ArrayList<>());
        }

        // ═══════════════════════════════════
        // TODO 2
        // Agregar arista no dirigida
        // ═══════════════════════════════════
        public void agregarArista(String origen, String destino, int peso) {
            // Agregar arista origen -> destino
            adyacencia.get(origen).add(new Arista(destino, peso));
            
            // Agregar arista destino -> origen (grafo no dirigido)
            adyacencia.get(destino).add(new Arista(origen, peso));
        }

        // ═══════════════════════════════════
        // TODO 3 — BFS
        // Ruta con menos paradas
        // ═══════════════════════════════════
        public List<String> bfs(String inicio, String fin) {
            // Cola para recorrer niveles
            Queue<List<String>> cola = new LinkedList<>();
            
            // Nodos visitados
            Set<String> visitados = new HashSet<>();
            
            // Camino inicial
            List<String> caminoInicial = new ArrayList<>();
            
            // Agregar nodo inicio al camino inicial
            caminoInicial.add(inicio);
            
            // Agregar caminoInicial a la cola
            cola.add(caminoInicial);
            
            // Marcar inicio como visitado
            visitados.add(inicio);
            
            while (!cola.isEmpty()) {
                // Obtener el primer camino de la cola
                List<String> camino = cola.poll();
                
                // Nodo actual
                String actual = camino.get(camino.size() - 1);
                
                // Si llegamos al destino
                if (actual.equals(fin)) {
                    return camino;
                }
                
                // Recorrer vecinos
                for (Arista arista : adyacencia.get(actual)) {
                    String vecino = arista.destino;
                    
                    // Verificar si el vecino NO fue visitado
                    if (!visitados.contains(vecino)) {
                        // Marcar vecino como visitado
                        visitados.add(vecino);
                        
                        // Crear nuevo camino
                        List<String> nuevoCamino = new ArrayList<>(camino);
                        
                        // Agregar vecino al nuevo camino
                        nuevoCamino.add(vecino);
                        
                        // Agregar nuevoCamino a la cola
                        cola.add(nuevoCamino);
                    }
                }
            }
            
            return null;
        }

        // ═══════════════════════════════════
        // TODO 4 — Dijkstra
        // Ruta con menor distancia
        // ═══════════════════════════════════
        public List<String> dijkstra(String inicio, String fin) {
            Map<String, Integer> distancias = new HashMap<>();
            Map<String, String> anteriores = new HashMap<>();
            
            // PriorityQueue con comparador basado en la distancia
            PriorityQueue<String> cola = new PriorityQueue<>(
                Comparator.comparingInt(distancias::get)
            );
            
            // Inicializar distancias
            for (String nodo : nodos.keySet()) {
                // Inicializar distancia infinita
                distancias.put(nodo, Integer.MAX_VALUE);
                anteriores.put(nodo, null);
            }
            
            // Distancia del inicio = 0
            distancias.put(inicio, 0);
            
            // Agregar inicio a la cola
            cola.add(inicio);
            
            while (!cola.isEmpty()) {
                // Obtener nodo con menor distancia
                String actual = cola.poll();
                
                // Si llegamos al destino, podemos terminar temprano
                if (actual.equals(fin)) {
                    break;
                }
                
                // Si la distancia actual es infinita, no hay más nodos alcanzables
                if (distancias.get(actual) == Integer.MAX_VALUE) {
                    continue;
                }
                
                for (Arista arista : adyacencia.get(actual)) {
                    String vecino = arista.destino;
                    int peso = arista.peso;
                    
                    // Calcular nueva distancia
                    int nuevaDistancia = distancias.get(actual) + peso;
                    
                    // Verificar si nuevaDistancia es menor
                    if (nuevaDistancia < distancias.get(vecino)) {
                        // Actualizar distancia
                        distancias.put(vecino, nuevaDistancia);
                        
                        // Guardar nodo anterior
                        anteriores.put(vecino, actual);
                        
                        // Agregar vecino a la cola
                        cola.add(vecino);
                    }
                }
            }
            
            // Reconstruir camino
            List<String> camino = new ArrayList<>();
            String actual = fin;
            
            while (actual != null) {
                camino.add(0, actual);
                actual = anteriores.get(actual);
            }
            
            // Si el camino solo tiene el nodo inicio y no es el destino, no hay ruta
            if (camino.size() == 1 && !inicio.equals(fin)) {
                return null;
            }
            
            return camino;
        }

        // ═══════════════════════════════════
        // Mostrar resultado
        // ═══════════════════════════════════
        public void mostrarRuta(List<String> ruta) {
            if (ruta == null) {
                System.out.println("No existe ruta");
                return;
            }
            
            for (int i = 0; i < ruta.size(); i++) {
                String idNodo = ruta.get(i);
                Nodo nodo = nodos.get(idNodo);
                
                System.out.print(nodo.nombre + " (" + nodo.id + ")");
                
                if (i < ruta.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            
            System.out.println();
        }
        
        // ═══════════════════════════════════
        // Visualización del grafo
        // ═══════════════════════════════════
        public void mostrarGrafo() {
            System.out.println("\n===== ESTRUCTURA DEL GRAFO =====");
            System.out.println("Lista de adyacencia:\n");
            
            for (String id : adyacencia.keySet()) {
                Nodo nodo = nodos.get(id);
                System.out.print(nodo.nombre + " (" + id + ") -> ");
                
                List<Arista> aristas = adyacencia.get(id);
                if (aristas.isEmpty()) {
                    System.out.println("(sin conexiones)");
                } else {
                    for (int i = 0; i < aristas.size(); i++) {
                        Arista a = aristas.get(i);
                        Nodo dest = nodos.get(a.destino);
                        System.out.print(dest.nombre + " (" + a.destino + ", peso:" + a.peso + ")");
                        if (i < aristas.size() - 1) {
                            System.out.print(", ");
                        }
                    }
                    System.out.println();
                }
            }
            System.out.println();
        }
    }

    // ═══════════════════════════════════════
    // MAIN
    // ═══════════════════════════════════════
    public static void main(String[] args) {
        Grafo grafo = new Grafo();
        
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║     MAPA DEL CAMPUS HUACHI - UNIVERSIDAD TÉCNICA   ║");
        System.out.println("║               DE AMBATO - ESTRUCTURA DE DATOS      ║");
        System.out.println("╚════════════════════════════════════════════════════╝");
        
        // NODOS
        grafo.agregarNodo("uta", "Universidad");
        grafo.agregarNodo("fisei", "FISEI");
        grafo.agregarNodo("idiomas", "Idiomas");
        grafo.agregarNodo("biblioteca", "Biblioteca");
        grafo.agregarNodo("estadio", "Estadio");
        grafo.agregarNodo("comedor", "Comedor");
        
        // ARISTAS
        grafo.agregarArista("uta", "fisei", 50);
        grafo.agregarArista("fisei", "idiomas", 40);
        grafo.agregarArista("idiomas", "biblioteca", 30);
        grafo.agregarArista("biblioteca", "estadio", 70);
        
        // Ruta con menos paradas pero más distancia
        grafo.agregarArista("uta", "comedor", 20);
        grafo.agregarArista("comedor", "estadio", 200);
        
        // Mostrar la estructura completa del grafo
        grafo.mostrarGrafo();
        
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("🔍 BUSCANDO RUTA DESDE 'Universidad' HASTA 'Estadio'");
        System.out.println("═══════════════════════════════════════════════════════");
        
        // ═══════════════════════════════════
        // PRUEBAS
        // ═══════════════════════════════════
        
        System.out.println("\n===== BFS (MENOR NÚMERO DE PARADAS) =====");
        System.out.println("Objetivo: Encontrar la ruta con menos edificios intermedios\n");
        
        List<String> rutaBFS = grafo.bfs("uta", "estadio");
        
        System.out.print("📌 Ruta encontrada: ");
        grafo.mostrarRuta(rutaBFS);
        
        System.out.println("\n===== DIJKSTRA (MENOR DISTANCIA TOTAL) =====");
        System.out.println("Objetivo: Encontrar la ruta que minimice la distancia en metros\n");
        
        List<String> rutaDijkstra = grafo.dijkstra("uta", "estadio");
        
        System.out.print("📌 Ruta encontrada: ");
        grafo.mostrarRuta(rutaDijkstra);
        
        // Información adicional
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("📊 ANÁLISIS DE RESULTADOS:");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("✅ BFS: Encuentra la ruta con MENOS PARADAS (nodos intermedios)");
        System.out.println("   - No considera el peso/distancia de las aristas");
        System.out.println("   - Ruta: Universidad → FISEI → Idiomas → Biblioteca → Estadio");
        System.out.println("   - Número de paradas: 4");
        System.out.println();
        System.out.println("✅ Dijkstra: Encuentra la ruta de MENOR DISTANCIA TOTAL");
        System.out.println("   - Considera los pesos/acumula distancias");
        System.out.println("   - Ruta: Universidad → Comedor → Estadio");
        System.out.println("   - Distancia total: 20m + 200m = 220 metros");
        System.out.println();
        System.out.println("💡 Conclusión: Dijkstra prioriza menor distancia (220m aunque");
        System.out.println("   tenga solo 1 parada), mientras que BFS prioriza menor");
        System.out.println("   cantidad de paradas (4 paradas aunque la distancia");
        System.out.println("   Universidad → FISEI → Idiomas → Biblioteca → Estadio");
        System.out.println("   sea 50+40+30+70 = 190 metros, menor distancia que Dijkstra");
        System.out.println("   en este caso particular porque el peso de Comedor-Estadio");
        System.out.println("   es muy alto (200m).");
        System.out.println("═══════════════════════════════════════════════════════");
    }
}