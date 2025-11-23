/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.calculadoramvc;
import static com.mycompany.calculadoramvc.Operacion.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.Timer;

/**
 *
 * @author kokoju
 */
public class CalculadoraLogic implements ActionListener {  // Clase encargada de manejar la lógica del Frame
    // Atributos
    private CalculadoraFrame cliente;  // Mantenemos una referencia al cliente
    private DataFrame dataFrame;  // Referencia vacía para el DataFrame
    private File archivoBitacora;  // Creamos también una referencia al archivo de bitácora dónde escribimos
    private File archivoMemoria;  // Se crea una referencia para la memoria
    private ArrayList<Double> arregloMemoria;  // Creamos un arreglo dónde guardar los elementos de memoria
    private boolean puedeRecibirInput;  // Booleano para indicar si el usuario puede escribir o no. Es necesario para dar errores
    
    // Constructor
    public CalculadoraLogic(CalculadoraFrame cliente) {
        this.cliente = cliente;
        this.dataFrame = new DataFrame();  // Referencia vacía para el DataFrame
        registroBotones();  // Registramos botones
        this.puedeRecibirInput = true;  // Por defecto, los botones están activos
        this.archivoBitacora = new File("bitacora.txt");
        this.archivoMemoria = new File("memoria.txt");
        this.arregloMemoria = leerNumerosMemoria();
    }
    
    // Métodos
    public void registrarEnBitacora(String mensaje) {  // Para la escritura de las operaciones en la bitácora, hacemos una función que escriba en el archivo
        try (FileWriter writer = new FileWriter(this.archivoBitacora, true)) {  // Intentamos crear una escritor en el archivo. Además, el 'true' indica que el contenido que pongamos se va a añadir por medio de un .append, permitiendo mantener registros anteriores
            writer.write(mensaje);  // Se escribe en el archivo
        } catch (IOException e) {  // Si hay una excepción al intentar escribir
            this.enviarError("NO SE PUEDE ESCRIBIR EN LA BITÁCORA");  // Si llega a existir un error al intentar escribir, se muestra en la pantalla de la calculadora
        }
    }
    
    public ArrayList<Double> leerNumerosMemoria() {  // Para leer nuestro archivo de memoria, creamos un Scanner que va a ir buscando números registrados
        ArrayList<Double> arregloObtenido = new ArrayList<>();  // Creamos un arreglo para almacenar lo obtenido
        try {
            Scanner sc = new Scanner(this.archivoMemoria);  // Se crea el Scanner
            while (sc.hasNextInt()) {  // Mientas siga habiendo números por leer 
                double num = sc.nextDouble();  // Se obtienen y se imprimen
                arregloObtenido.add(num);
            }
            sc.close();  // Cerramos el archivo
            return arregloObtenido;  // Devolvemos el arreglo
        } catch (FileNotFoundException e) {
            this.enviarError("NO SE PUEDE ACCEDER A LA MEMORIA");
            return null;
        }
    }
    
    public void insertarElementoMemoria(double num) {  // Función encargada de tomar un double y añadirlo a la memoria. Si esta se pasa de 10, resta el elemento más antiguo
        this.arregloMemoria.addFirst(num);
        if (this.arregloMemoria.size() > 10)  // Si nos pasamos del límite
            this.arregloMemoria.removeLast();  // Eliminamos el elemento más antiguo 
        this.registrarEnMemoria();
        this.actualizarEnData();
    }
    
    public void registrarEnMemoria()  {  // Así como tomamos de la memoria, debemos escribir el contenido que tenemos
        try (FileWriter writer = new FileWriter(this.archivoMemoria, false)) {  // Intentamos crear una escritor en el archivo. El 'false' borra todo el contenido que estaba anteriormente
            for (Double num : arregloMemoria) {
                writer.write(num + "\n");  // Se escribe en el archivo, saltando espacios por cada número
            }
        } catch (IOException e) {  // Si hay una excepción al intentar escribir
            this.enviarError("NO SE PUEDE ESCRIBIR EN LA MEMORIA");  // Si llega a existir un error al intentar escribir, se muestra en la pantalla de la calculadora
        }
    }
    
    public void actualizarEnData() {  // El espacio de texto de DataFrame debe tener la info actualizada, entonces eso se llama en cada iteración
        this.dataFrame.getTxaData().setText("");  // Se limpia el espacio
        for (double num : arregloMemoria)  // Se escribe cada número en el espacio
            this.dataFrame.getTxaData().setText(this.dataFrame.getTxaData().getText() + num + "\n");
    }

    public void registroBotones() {
        // setActionCommand() sirve le asigna un nombre String a la acción que dispara un botón, por lo que eso usaremos para hacer un switch después
        // De normal, getActionCommand() tiene el texto del botón, pero lo establecemos de otra manera para mayor facilidad
        this.cliente.getBtn0().setActionCommand("btn0");
        this.cliente.getBtn1().setActionCommand("btn1");
        this.cliente.getBtn2().setActionCommand("btn2");
        this.cliente.getBtn3().setActionCommand("btn3");
        this.cliente.getBtn4().setActionCommand("btn4");
        this.cliente.getBtn5().setActionCommand("btn5");
        this.cliente.getBtn6().setActionCommand("btn6");
        this.cliente.getBtn7().setActionCommand("btn7");
        this.cliente.getBtn8().setActionCommand("btn8");
        this.cliente.getBtn9().setActionCommand("btn9");
        this.cliente.getBtnAdd().setActionCommand("btnAdd");
        this.cliente.getBtnAverage().setActionCommand("btnAverage");
        this.cliente.getBtnBinary().setActionCommand("btnBinary");
        this.cliente.getBtnClear().setActionCommand("btnClear");
        this.cliente.getBtnData().setActionCommand("btnData");
        this.cliente.getBtnDiv().setActionCommand("btnDiv");
        this.cliente.getBtnDot().setActionCommand("btnDot");
        this.cliente.getBtnMAdd().setActionCommand("btnMAdd");
        this.cliente.getBtnMul().setActionCommand("btnMul");
        this.cliente.getBtnPrime().setActionCommand("btnPrime");
        this.cliente.getBtnRes().setActionCommand("btnRes");
        this.cliente.getBtnSpace().setActionCommand("btnSpace");
        this.cliente.getBtnSub().setActionCommand("btnSub");
        this.cliente.getBtnSubNum().setActionCommand("btnSubNum");
        
        
        // Registro de todos los botones para que sean escuchados en la lógica
        for (JButton boton : this.cliente.getArregloBtns()) {
            boton.addActionListener(this);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (puedeRecibirInput) {
            String source = e.getActionCommand();
            switch(source) {
                case "btn0" -> actionBtn0();
                case "btn1" -> actionBtn1();
                case "btn2" -> actionBtn2();
                case "btn3" -> actionBtn3();
                case "btn4" -> actionBtn4();
                case "btn5" -> actionBtn5();
                case "btn6" -> actionBtn6();
                case "btn7" -> actionBtn7();
                case "btn8" -> actionBtn8();
                case "btn9" -> actionBtn9();
                case "btnAdd" -> actionBtnAdd();
                case "btnAverage" -> actionBtnAverage();
                case "btnBinary" -> actionBtnBinary();
                case "btnClear" -> actionBtnClear();
                case "btnData" -> actionBtnData();
                case "btnDiv" -> actionBtnDiv();
                case "btnDot" -> actionBtnDot();
                case "btnMAdd" -> actionBtnMAdd();
                case "btnMul" -> actionBtnMul();
                case "btnPrime" -> actionBtnPrime();
                case "btnRes" -> actionBtnRes();
                case "btnSpace" -> actionBtnSpace();
                case "btnSub" -> actionBtnSub();
                case "btnSubNum" -> actionBtnSubNum();
            }
        }
        

    }
    
    public void actionBtn0() {
        this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + "0");
    }
    
    public void actionBtn1() {
        this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + "1");
    }
    
    public void actionBtn2() {
        this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + "2");
    }
    
    public void actionBtn3() {
        this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + "3");
    }
    
    public void actionBtn4() {
        this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + "4");
    }
    
    public void actionBtn5() {
        this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + "5");
    }
    
    public void actionBtn6() {
        this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + "6");
    }
    
    public void actionBtn7() {
        this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + "7");
    }
    
    public void actionBtn8() {
        this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + "8");
    }
    
    public void actionBtn9() {
        this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + "9");
    }
    
    public void actionBtnAdd() {
        if (verificarSiOpValida())
            actionBtnRes();  // Llama a la operación de resultado, la cual hace algo si la operación es válida
        if (this.puedeRecibirInput)  // Si al llamar a la acción no hubo errores, se imprime el símbolo
            this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + " + ");
    }
    
    public void actionBtnAverage() {        
        String[] partes = this.obtenerPartes();  // Se obtienen las partes, separadas por espacios
        boolean huboError = false;  // Booleano que lleva constancia de si hubo un fallo
        if (partes.length > 0) {  // Si hay algún elemento
            double resultado = 0;  // Establecemos un resultado
            for (String parte : partes) {  // Para cada parte obtenida
                try {  // Intentamos sumarla
                    resultado += Double.parseDouble(parte);
                } catch (NumberFormatException e) {  // Si se intenta parsear algo que no es un número, como un signo, se da error
                    this.enviarError("CARÁCTER INVÁLIDO, REINTENTAR");
                    huboError = true;
                }    
            }
            if (!huboError) {
                resultado /= partes.length;  // Se divide el resultado obtenido por la cantidad de elementos: esto permite conseguir el promedio
                this.cliente.getTxaPantalla().setText(resultado + "");
                this.registrarEnBitacora("Promedio de ");  // Se escribe la operación en la bitácora
                for (String parte : partes)
                    this.registrarEnBitacora(parte + " ");  // Se van mostrando los números
                this.registrarEnBitacora(" = " + resultado + "\n");  // Se escribe el resultado
            }
        }
    }
    
    public void actionBtnBinary() {
        if (verificarSiOpValida())
            actionBtnRes();  // Llama a la operación de resultado, la cual hace algo si la operación es válida
        if (this.puedeRecibirInput) {  // Si al llamar a la acción no hubo errores, se imprime el símbolo
            String[] partes = obtenerPartes();
            if (partes.length > 0) {
                double num = Double.parseDouble(partes[0]);
                String resultadoEntero = obtenerBinarioEntero(num);
                String resultadoFraccionario = obtenerBinarioFraccionario(num);
                this.cliente.getTxaPantalla().setText(resultadoEntero + "." + resultadoFraccionario);
            }
            else {
                this.enviarError("NO HAY ARGUMENTOS, REINTENTAR");
            }
        }
    }
    
    public void actionBtnClear() {
        this.cliente.getTxaPantalla().setText("");
    }
    
    public void actionBtnData() {
        dataFrame.setVisible(true);
    }
    
    public void actionBtnDiv() {
        if (verificarSiOpValida())
            actionBtnRes();  // Llama a la operación de resultado, la cual hace algo si la operación es válida
        if (this.puedeRecibirInput)  // Si al llamar a la acción no hubo errores, se imprime el símbolo
            this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + " ÷ ");
    }
    
    public void actionBtnDot() {     
        this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + ".");
    }
    
    public void actionBtnMAdd() {
        // this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + " ÷ ");
    }
    
    public void actionBtnMul() {
        if (verificarSiOpValida())
            actionBtnRes();  // Llama a la operación de resultado, la cual hace algo si la operación es válida
        if (this.puedeRecibirInput)  // Si al llamar a la acción no hubo errores, se imprime el símbolo
            this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + " * ");
    }
    
    public void actionBtnPrime() {
        if (verificarSiOpValida())
            actionBtnRes();  // Llama a la operación de resultado, la cual hace algo si la operación es válida
        if (this.puedeRecibirInput) {  // Si al llamar a la acción no hubo errores, se imprime el símbolo
            String[] partes = obtenerPartes();
            if (partes.length > 0) {
                double num = Double.parseDouble(partes[0]);  // Transformarmos el texto que tenemos en un double
                boolean esPrimo = this.verificarSiNumPrimo(num);  // Vemos si nuestro número es primo
                this.enviarMensaje(esPrimo ? "EL NÚMERO INTRODUCIDO ES PRIMO (" + num + ")" : "EL NÚMERO INTRODUCIDO ES NO PRIMO (" + num + ")");
            }
            else {
                this.enviarError("NO HAY ARGUMENTOS, REINTENTAR");
            }
        }
        
    }
    
    public void actionBtnRes() {
        if (verificarSiOpValida()) {  // Si la operación tiene 3 argumentos
            String[] partes = obtenerPartes();  // Obtenemos las partes
            boolean huboError = false;  // Booleano que lleva constancia de si hubo error
            
            // Las separamos en variables para claridad
            double a = 0;
            double b = 0;
            
            try {  // Intentamos asignarle valores a 'a' y a 'b'
                a = Double.parseDouble(partes[0]);
                b = Double.parseDouble(partes[2]);
            }
            catch (NumberFormatException e) {
                this.enviarError("ARGUMENTOS INCORRECTOS, REINTENTAR");
                huboError = true;
            }
            if (!huboError) {  // Si no hubo error, se hace la operación y se muestra
                Operacion op = Operacion.desdeSimbolo(partes[1]);  // Obtenemos la operación que debemos hacer
                double resultado = op.aplicarOperacion(a, b);  // Obtenemos resultado
                this.cliente.getTxaPantalla().setText(resultado + "");  // Escribimos el resultado en pantalla 
                this.registrarEnBitacora(a + op.getSimbolo() + b + " = " + resultado + "\n");  // Se escribe la operación en la bitácora
            }
        }
        else {
            this.enviarError("OPERACIÓN INVÁLIDA, REINTENTAR");
        }
    }
    
    public void actionBtnSpace() {
        this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + " ");
    }
    
    public void actionBtnSub() {
        if (verificarSiOpValida())
            actionBtnRes();  // Llama a la operación de resultado, la cual hace algo si la operación es válida
        if (this.puedeRecibirInput)  // Si al llamar a la acción no hubo errores, se imprime el símbolo
            this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + " - ");
    }
    
    public void actionBtnSubNum() {
        this.cliente.getTxaPantalla().setText(this.cliente.getTxaPantalla().getText() + "-");
    }
    
 
    // Funciones para el funcionamiento de los botones
    public String[] obtenerPartes() {  // Toma el texto de la pantalla y lo separa en argumentos, los que sea que haya
        String input = this.cliente.getTxaPantalla().getText().trim();  // .trim() quita los espacios innecesarios, tanto al inicio como al final
        
        if (input.isEmpty()) {  // Si el input está vacío, se devuelve un arreglo vacío. Se hace porque el split siempre da mínimo 1, afectando el funcionamiento de otras funciones
            return new String[0];
        }
        
        String[] partes = input.split(" ");  // Obtenemos cuantas partes tiene nuestro contenido actual
        return partes;
    }
    
    public boolean verificarSiEsPrimerElemento() {  // Un operando NO puede ser el primer elemento escrito, entonces lo verificamos
        String[] partes = obtenerPartes();
        return (partes.length == 0);  // Si no hay nada a la hora de intentar poner el operando, se devuelve true
    }
    
    public boolean verificarSiOpValida() {
        String[] partes = obtenerPartes();
        return (partes.length == 3);  // Si el tamaño que tenemos es igual a 3 (2 operandos y un operador), hacemos la operación indicada por el signo de la mitad, de índice 1)
    }

    public void setPuedeRecibirInput(boolean bool) {  // Función para establecer si se puede recibir input
        this.puedeRecibirInput = bool;
    }
    
    public boolean verificarSiNumPrimo(double num) {  // Verificación para saber si un número es primo, tomado de https://es.stackoverflow.com/questions/34895/determinar-si-el-n%C3%BAmero-es-primo
        if (num % 2 == 0) return false;  // Si el número es par, todo par inferior lo va a dividir, entonces no es 

        for(int i = 3; i * i <= num; i += 2) {  // Si es impar, se revisan posiciones impares hasta llegar a la mitad del número, ya que un número par no puede dividir a uno impar (empezando desde 3, el primer primo impar)
        // Este método se basa en la lógica de que si hay un par de números que se multipĺican para hacer el número que tenemos, existe una pareja pequeña es menor o igual a la raíz cuadrada 
        // Si un número NO tiene divisores pequeños (menores o iguales a √(num)), entonces NO tiene divisores grandes tampoco y es primo
        // Cabe recordar que i * i <= num equivale a √(i * i) <= √(num), que a su vez equivale a i <= √(num), siendo esto lo que buscabamos
            if(num % i == 0)  // Si en algún caso se da que un número pequeño divide a nuestro número 
                return false;  // No es primo
        }
    return true;  // Si llegamos aquí, recorrimos hasta la raíz y no encontramos ningún divisor, entonces
    }

    public boolean verificarSiTieneFraccionaria(double num) {  // Verificación para saber si un número tiene una parte fraccionaria
        return (Math.floor(num) != num);  // Si el número al ser redondeado es igual, significa que no tiene parte fraccionaria 
    }
        
    public double obtenerParteFraccionaria(double num) {  // Obtiene la parte fraccionaria de un número
        return (num - Math.floor(num));
    }

    public String obtenerBinarioEntero(double num) {  // Recibe un número y lo pasa al binario
        num -= obtenerParteFraccionaria(num);  // Para evitar redondeos, vamos hacía abajo
        String cadena = "";  // Creamos una cadena dónde introducir nuestra construccion binaria
        while (num >= 1) {  // Mientras num sea superior a 0
            num /= 2;
            cadena = (verificarSiTieneFraccionaria(num) ? "1" : "0") + cadena;  // Vamos dividiendo y buscando el residuo obtenido
            if (verificarSiTieneFraccionaria(num))  // Si tuvo una parte fraccionaria, debemos quitársela para que el algoritmo siga funcionando
                num -= obtenerParteFraccionaria(num);  
               
        }
        if ("".equals(cadena))  // Si la cadena está vacía, devolver 0 
            return "0"; 
        return cadena;
    }
    
    public String obtenerBinarioFraccionario(double num)  {  // Recibe un número, toma su parte fraccionaria y la transforma al binario, con cierta precisión
        num = obtenerParteFraccionaria(num);  // Del número que se nos pase, le tomamos la parte fraccionaria
        int PRECISION = 10;  // Definimos una precisión
        int precisionActual = 0;  // Establecemos un contador de precisión
        String cadena = "";  // Así como con la parte entera, hacemos una cadena de texto
        while (num != 0 && precisionActual < PRECISION) {
            num *= 2;
            double numEntero = num - obtenerParteFraccionaria(num);  // Vamos multiplicando
            cadena = (numEntero == 1.0 ? "1" : "0") + cadena;  // Añadimos 1 en función del dígito en la unidad
            if (numEntero == 1.0)  // Si ya pusimos un 1
                num -= 1;  // Lo restamos para que el algoritmo siga teniendo sentido
            precisionActual += 1;  // Hicimos otra iteración, entonces incrementamos la precisión que llevamos
        }
        if ("".equals(cadena))  // Si la cadena está vacía, devolver 0 
            return "0"; 
        return cadena;
    }
    
    // Funciones para enviar mensajes
    public void enviarError(String error) {
        this.setPuedeRecibirInput(false);
        this.cliente.getTxaPantalla().setForeground(Color.RED);  // Para el error, establecemos el color del texto en rojo
        this.cliente.getTxaPantalla().setText("ERROR: " + error);
        Timer timerError = new javax.swing.Timer(1500, e -> {  // Una vez impreso, desactivamos la calculadora 1.5s para poder indicar el error al usuario
            this.setPuedeRecibirInput(true);
            this.cliente.getTxaPantalla().setForeground(Color.BLACK);  // El texto vuelve a ser negro y se limpia la pantalla al terminar el error
            this.cliente.getTxaPantalla().setText("");
            
        });
        // Iniciar el Timer
        timerError.setRepeats(false);  // Ponemos que no pueda repetirse, ejecutándose así una sola vez
        timerError.start();
    }
    
    public void enviarMensaje(String mensaje) {
        this.setPuedeRecibirInput(false);  // Se evita que el user llame al texto
        this.cliente.getTxaPantalla().setText(mensaje);  // Se imprime el mensaje
        Timer timerMensaje = new javax.swing.Timer(2500, e -> {  // Una vez impreso, desactivamos la calculadora 2.5s
            this.setPuedeRecibirInput(true);  // Se reactiva el texto
            this.cliente.getTxaPantalla().setText("");  // Se limpia la pantalla
            
        });
        // Iniciar el Timer
        timerMensaje.setRepeats(false);  // Ponemos que no pueda repetirse, ejecutándose así una sola vez
        timerMensaje.start();
    }
}

/*
String input = txa.getText().trim();
String[] partes = input.split(" ");

int a = Integer.parseInt(partes[0]);
String op = partes[1];
int b = Integer.parseInt(partes[2]);
*/