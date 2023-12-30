```
********************************************************************** 
|        _ _____ _      _        _   ____            _               | 
|       | |_   _(_) ___| | _____| |_|  _ \  ___  ___(_) __ _ _ __    | 
|    _  | | | | | |/ __| |/ / _ \ __| | | |/ _ \/ __| |/ _` | '_ \   | 
|   | |_| | | | | | (__|   <  __/ |_| |_| |  __/\__ \ | (_| | | | |  | 
|    \___/  |_| |_|\___|_|\_\___|\__|____/ \___||___/_|\__, |_| |_|  | 
|                                                      |___/         | 
********************************************************************** 
```

[![Maven Central](https://img.shields.io/maven-central/v/pe.puyu/JTicketDesing.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/pe.puyu/JTicketDesing)<br>
JTicketDesing genera varios tipos de diseño de tickets para puntos de venta
apartir de un objeto json, [vea Modelos de tickets soportados](#modelos-de-tickets-soportados), Se apoya en
la libreria [escpos coffee](https://github.com/anastaciocintra/escpos-coffee)
para la generación de comandos escpos.

1. [¿Cómo agrego a mi proyecto?](#como-agregó-a-mi-proyecto)
2. [Uso](#uso)
3. [Caracteristicas configurables](#caracteristicas-configurables)
4. [Modelos de tickets soportados](#modelos-de-tickets-soportados)
   1. [Estructura general](#estructura-general)
   2. [Ejemplos de json validos](#ejemplos-de-json-validos)
5. [Considerar logo y QR en el diseño](#considerar-logo-yo-código-qr-en-el-diseño-de-boletas-y-facturas)

## ¿Como agregó a mi proyecto?

JTicketDesign esta disponible como dependencia en Maven Central.
Agrega lo siguiente a tu pom.xml

```xml

<dependency>
  <groupId>pe.puyu</groupId>
  <artifactId>JTicketDesing</artifactId>
  <version><!--Aqui va la version, ejm: 0.2.0--></version>
</dependency>
```

> Ultima
> versión: [![Maven Central](https://img.shields.io/maven-central/v/pe.puyu/JTicketDesing.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/pe.puyu/JTicketDesing)

## Uso

1. JTicketDesign espera como parametro un JSONObject, que es la representación
   del ticket en formato json. [vea Modelos de tickets soportados](#modelos-de-tickets-soportados).
2. El diseño del ticket sera devuelto en bytes de comandos escpos con la
   llamada al metodo getBytes().
3. Los bytes escpos ahora pueden ser escritos en cualquier objeto que sea
   instancia de clases que hereden de la clase abstracta OutputStream.
   Por ejemplo socket.getOutputStream(), PrinterOutputStream, SerialStream,
   PipedOuputStream, etc.<br>
   > Nota: Los bytes al ser comandos escpos no pueden ser enviados directamente a
   la consola ya que no seran interpretados correctamente, al ser comandos
   escpos tienen que ser enviados a un OutputStream asociado a una ticketera con
   soporte de comandos EscPos.

### Ejemplo

```java
import com.github.anastaciocintra.output.TcpIpOutputStream;
import org.json.JSONObject;
import pe.puyu.jticketdesing.core.SweetTicketDesign;

import java.io.OutputStream;

public class Main {
	public static void main(String[] args) {
		try (OutputStream outputStream = new TcpIpOutputStream("192.168.1.100", 9100)) {
			String jsonString = any.getTicket();
			JSONObject ticket = new JSONObject(jsonString);
			var desing = new SweetTicketDesign(ticket);
			outputStream.write(desing.getBytes());
		}
	}
}
```

## Caracteristicas configurables

Se puede personalizar 7 caracteristicas, las cuales estarán presentes en el json,
[ver modelos de tickets soportados](#modelos-de-tickets-soportados) para ver como configurarlos.

| Propiedad            | Tipo    | Por defecto | Descripción                                                                                                                                                                                                                                         |
|----------------------|---------|-------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| width                | int     | 42          | Número de caracteres por linea, jugar con este valor segun el tamaño del ticket, por defecto para ticketeras de 80mm, 42 es la valor  deseado.                                                                                                      |
| backgroundInverted   | boolean | true        | Algunas ticketeras antiguas fallan al imprimir anulaciones de comandas, si es es el caso configurar esta propiedad a false.                                                                                                                         |
| charCodeTable        | string  | WPC1252     | El tipo de codificación de caracteres según escpos coffee, [enum CharCodeTable, escpos coffee](https://github.com/anastaciocintra/escpos-coffee/blob/master/src/main/java/com/github/anastaciocintra/escpos/EscPos.java).                           |
| fontSizeCommand      | int     | 2           | Representa el tamaño de fuente que tendran las comandas, por cuestiones de enfasis en los pedidos el valor por defecto es 2, pero se puede establecer en 1.                                                                                         |
| nativeQR             | boolean | false       | Por defecto el qr es generado como imagen y tratado como tal, pero tambien se puede establecer a true para que escpos coffee sea quien genere el qr. Se recomienda el valor por defecto ya que no todas la ticketeras soportan qr nativo de escpos. |
| blankLinesAfterItems | int     | 0           | Número de lineas en blanco despues de imprimir los items, por defecto 0. Util en proformas.                                                                                                                                                         |
| showUnitPrice        | boolean | false       | Indica si se debe mostrar el precio unitario en la tabla items, y cada item debe tener una propiedad "unitPrice".   Por defecto false.                                                                                                              |

## Modelos de tickets soportados

SweetTicketDesign tiene varios modelos de tickets establecidos: boletas, facturas
extras, encomienda, delivery, comandas para restaurante, extra para restaurante y precuentas.
El modelo que se diseñara dependera del objeto json y su propiedad type.

### Estructura general

Un objeto json ticket puede estar compuesta por varias propiedades, la mayoria de las propiedades
son opcionales, Las propiedades que se tomaran en cuenta depende del
tipo de ticket a diseñar (type). [ver ejemplos de json validos](#ejemplos-de-json-validos).

#### Interfaz del ticket

```typescript
interface Ticket {
    type: string; // invoice, note, command, precount, extra 
    times: int; // numero de veces a diseñar del ticket
    printer: {
        properties: {
            width: int // Opcional, por defecto 42
            backgroundInverted: boolean;// Opcional, por defecto true
            charCodeTable: string;// Opcional, por defecto WP1252
            fontSizeCommand: int; // Opcional, por defecto 2
            nativeQR: boolean;// Opcional, por defecto false
            blankLinesAfterItems: int; // opcional, por defecto 0
        }
    }
    data: {
        business: { // opcional
            comercialDescription: { //opcional
                type: "text" | "img";// valores permitidos text y img
                value: string;
            }
            description: string;
            additional: string[];// opcional 
            document: string;// opcional
            documentNumber: string//opcional
        }
        titleExtra: { //opcional, solo para extras
            title: string;
            subtitle: string;
        }
        productionArea: string // opcional, obligatorio en comandas;
        textBackgroundInverted: string // opcional,obligatorio en comandas anulacion ;
        document: { //opcional
            description: string;
            identifier: string;
        }
        customer: string[];
        additional: string[]; //Array de strings, opcional
        items: [  // Array de objetos opcional
            {
                quantity: int, // opcional
                description: string,
                totalPrice: decimal,
            }, //...
        ]
        amounts: {};// Objeto generico ver ejemplos, opcional
        additionalFooter: string[] // opcional;
        finalMessage: string | string[] // opcional;
        stringQR: string;// opcional
        metadata: { //opcional
            logoPath: string; //opcional   
        }
    }
}
```

> Nota: Lo anterior solo es una especificación de que propiedades tendria
> que contener los objetos json, ver [ejemplos](#ejemplos-de-json-validos) a continuación.

### Ejemplos de json validos

1. [Boleta o factura](docs/printing-models.md#1-modelo-para-boleta-o-factura)
2. [Extras](docs/printing-models.md#2-modelo-para-extras)
3. [Encomienda](docs/printing-models.md#3-modelo-para-encomienda)
4. [Comanda restaurante](docs/printing-models.md#4-comanda-restaurante)
5. [Extras o delivery restaurante](docs/printing-models.md#5-modelo-de-extra-o-delivery-restaurante)
6. [Precuenta restaurante](docs/printing-models.md#6-modelo-precuenta-restaurante)
7. [Notas de venta](docs/printing-models.md#7-notas-de-venta)

### Considerar logo y/o código QR en el diseño de boletas y facturas.

```javascript
{
    type: "invoice"
    //... las demas propiedades van aqui
    data: {
        business: {
            comercialDescription: {
                type: "img"; // obligatorio para el logo
            }
        }
       //... las demas propiedaes van aqui
       stringQR:"20450523381|01|F001|00000006|0|9.00|30/09/2019|6|sdfsdfsdf|"
    }
    metadata: {
        logoPath: "C:\\Imagenes\\logo.png"
    }
}
```

