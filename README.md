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
JTicketDesing genera varios tipos de diseño de tickets para puntos de venta
apartir de un objeto json, [vea Modelos de tickets soportados](#modelos-de-tickets-soportados), Se apoya en
la libreria [escpos coffee](https://github.com/anastaciocintra/escpos-coffee)
para la generación de comandos escpos.

1. [¿Cómo agrego a mi proyecto?](#como-agregó-a-mi-proyecto)
2. [Uso](#uso)
3. [Caracteristicas configurables](#caracteristicas-configurables)
4. [Modelos de tickets soportados](#modelos-de-tickets-soportados)
5. [Considerar logo y QR en el diseño](#considerar-logo-yo-código-qr-en-el-diseño-de-boletas-y-facturas)

## ¿Como agregó a mi proyecto?

JTicketDesign esta disponible como dependencia en Maven Central.
Agrega lo siguiente a tu pom.xml

```xml

<dependency>
  <groupId>pe.puyu</groupId>
  <artifactId>JTicketDesing</artifactId>
  <version>0.1.0</version>
</dependency>
```

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

Se puede personalizar 5 caracteristicas, las cuales estarán presentes en el json,
[ver modelos de tickets soportados](#modelos-de-tickets-soportados) para ver como configurarlos.

| Propiedad          | Tipo    | Por defecto | Descripción                                                                                                                                                                                                                                         |
|--------------------|---------|-------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| width              | int     | 42          | Número de caracteres por linea, jugar con este valor segun el tamaño del ticket, por defecto para ticketeras de 80mm, 42 es la valor  deseado.                                                                                                      |
| backgroundInverted | boolean | true        | Algunas ticketeras antiguas fallan al imprimir anulaciones de comandas, si es es el caso configurar esta propiedad a false.                                                                                                                         |
| charCodeTable      | string  | WPC1252     | El tipo de codificación de caracteres según escpos coffee, [enum CharCodeTable, escpos coffee](https://github.com/anastaciocintra/escpos-coffee/blob/master/src/main/java/com/github/anastaciocintra/escpos/EscPos.java).                           |
| fontSizeCommand    | int     | 2           | Representa el tamaño de fuente que tendran las comandas, por cuestiones de enfasis en los pedidos el valor por defecto es 2, pero se puede establecer en 1.                                                                                         |
| nativeQR           | boolean | false       | Por defecto el qr es generado como imagen y tratado como tal, pero tambien se puede establecer a true para que escpos coffee sea quien genere el qr. Se recomienda el valor por defecto ya que no todas la ticketeras soportan qr nativo de escpos. |

## Modelos de tickets soportados

SweetTicketDesing tiene varios modelos de tickets establecidos: boletas, facturas
extras, encomienda, delivery, comandas para restaurante, extra para restaurante y precuentas.
El modelo que se diseñara dependera del objeto json y su propiedad type.

### Estructura general<br>

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
    }
    productionArea: string // opcional, obligatorio en comandas;
    textBackgroundInverted: string // opcional,obligatorio en comandas anulacion ;
    document: { //opcional
        description: string;
        identifier: strign;
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
```

> Nota: Lo anterior solo es una especificación de que propiedades tendria
> que contener los objetos json, [ver ejemplos](#ejemplos-de-json-validos).

### Ejemplos de json validos

* Modelo para boleta o factura, type: invoice

```json
{
  "type": "invoice",
  "times": 1,
  "printer": {
    "properties": {
      "width": 42,
      "backgroundInverted": true,
      "charCodeTable": "WPC1252",
      "fontSizeCommand": 2,
      "nativeQR": false
    }
  },
  "data": {
    "business": {
      "comercialDescription": {
        "type": "text",
        "value": "REY DE LOS ANDES"
      },
      "description": "EMPRESA DE TRANSPORTES REY DE LOS ANDES S.A.C.",
      "additional": [
        "RUC 20450523381 AGENCIA ABANCAY",
        "DIRECCIÓN : Av. Brasil S/N",
        "TELÉFONO : 989290733"
      ]
    },
    "document": {
      "description": "Boleta de Venta\r ELECTRONICA",
      "identifier": "B001 - 00000071"
    },
    "customer": [
      "ADQUIRIENTE",
      "DNI: 20564379248",
      "FASTWORKX SRL",
      "AV CANADA N 159 ABANCAY ABANCAY APURIMAC"
    ],
    "additional": [
      "FECHA EMISIÓN : 01/10/2019 14:51:26",
      "MONEDA : SOLES",
      "USUARIO : "
    ],
    "items": [
      {
        "description": [
          "Ruta : ABANCAY-CHALHUANCA",
          "Embarque : ABANCAY",
          "Destino : CHALHUANCA",
          "Asiento : 2",
          "Pasajero : EMERSON ÑAHUINLLA VELASQUEZ",
          "DNI : 70930383",
          "F. Viaje : 01/10/2019 02:00 PM"
        ],
        "totalPrice": "9.00"
      }
    ],
    "amounts": {
      "Operacion no gravada": "9.00",
      "IGV": 0,
      "Total": "9.00"
    },
    "finalMessage": [
      "REPRESENTACIÓN IMPRESA COMPROBANTE ELECTRÓNICO",
      "PARA CONSULTAR EL DOCUMENTO VISITA NEXUS",
      "HTTPS://NEXUS.FASTWORKX.COM/20450523381",
      "RESUMEN: null",
      "",
      "POR FASTWORKX S.R.L. - PERÚ"
    ],
    "stringQR": "20450523381|03|B001 - 00000071|0|9.00|01/10/2019|6|[object Object]|"
  }
}

```

* Modelo para extras, type: invoice, con caracteristicas por defecto

```json
    {
  "type": "invoice",
  "times": 1,
  "printer": {
  },
  "data": {
    "business": {
      "comercialDescription": {
        "type": "text",
        "value": "REY DE LOS ANDES"
      },
      "description": "EMPRESA DE TRANSPORTES REY DE LOS ANDES S.A.C.",
      "document": "RUC",
      "documentNumber": "20450523381"
    },
    "document": {
      "description": "Control de",
      "identifier": "B001 - 00000071"
    },
    "additional": [
      "FECHA EMISIÓN : 01/10/2019 14:51:26",
      "USUARIO : admin"
    ],
    "items": [
      {
        "description": [
          "Embarque : ABANCAY",
          "Destino : CHALHUANCA",
          "Asiento : 2",
          "Pasajero : EMERSON ÑAHUINLLA VELASQUEZ",
          "F. Viaje : 01/10/2019 02:00 PM",
          "Conductor : QUISPE CONTRERAS GUILLERMO",
          "Bus : TOYOTA  HIACE PLACA D6R-954",
          ""
        ],
        "totalPrice": "9.00"
      }
    ],
    "finalMessage": "*** CONTROL DE BUS ***"
  }
}
```

* Modelo para encomienda, type: invoice, con ancho de ticket 30

```json
{
  "type": "invoice",
  "times": 1,
  "printer": {
    "properties": {
      "width": 30
    }
  },
  "data": {
    "document": {
      "description": "Boleta de Venta\r ELECTRONICA",
      "identifier": "B001 - 00000071"
    },
    "business": {
      "comercialDescription": {
        "type": "text",
        "value": "REY DE LOS ANDES"
      },
      "description": "EMPRESA DE TRANSPORTES REY DE LOS ANDES S.A.C.",
      "additional": [
        "RUC 20450523381 AGENCIA ABANCAY",
        "DIRECCIÓN : Av. Brasil S/N",
        "TELÉFONO : 989290733"
      ]
    },
    "customer": [
      "REMITENTE / CLIENTE",
      "DNI: 20564379248",
      "FASTWORKX SRL",
      "AV CANADA N 159 ABANCAY ABANCAY APURIMAC"
    ],
    "additional": [
      "FECHA EMISIÓN : 01/10/2019 14:51:26",
      "MONEDA : SOLES",
      "CONSIGNADO : RENZO ZABALA"
    ],
    "items": [
      {
        "description": "Tipo : Cajas cerradas",
        "quantity": 2,
        "totalPrice": "20.00"
      },
      {
        "description": "Giro de dinero",
        "quantity": 1,
        "totalPrice": "5.00"
      }
    ],
    "amounts": {
      "Operacion no gravada": "25.00",
      "IGV": 0,
      "Total": "25.00"
    },
    "additionalFooter": [
      "FECHA IMPR: 02/10/2019 16:12:34",
      "USUARIO : ADMIN | AGENCIA : ABANCAY"
    ],
    "finalMessage": [
      "REPRESENTACIÓN IMPRESA DE FACTURA ELECTRÓNICA",
      "PARA CONSULTAR EL DOCUMENTO VISITA NEXUS:",
      "HTTPS://NEXUS.FASTWORKX.COM/20450523381",
      "RESUMEN: Bfdfg+sdfsAfKfVs=",
      "",
      "POR FASTWORKX S.R.L. - PERÚ"
    ],
    "stringQR": "20450523381|01|F001|00000006|0|9.00|30/09/2019|6|sdfsdfsdf|"
  }
}
```

* Comanda para restaurante para una ticketera sin soporte de backgroundInverted

```json
{
  "type": "command",
  "times": 1,
  "printer": {
    "backgroundInverted": false
  },
  "data": {
    "business": {
      "description": "Restaurant H. Pollos"
    },
    "productionArea": "Pizzeria Horno",
    "textBackgroundInverted": "ANULACION",
    "document": {
      "description": "COMANDA : ",
      "identifier": "71"
    },
    "additional": [
      "FECHA EMISIÓN : 01/10/2019 14:51:26",
      "Mesero(a) : Luis",
      "Mesa : Delivery"
    ],
    "items": [
      {
        "quantity": 1,
        "description": "HAWAYANA (FAMILIAR)",
        "commentary": "con arto queso"
      },
      {
        "quantity": 1,
        "description": "HAWAYANA (PERSONAL)"
      }
    ]
  }
}
```

* Modelo de extra para restaurante

```json
{
  "type": "extra",
  "times": 1,
  "printer": {
  },
  "data": {
    "business": {
      "description": "Restaurant H. Pollos"
    },
    "titleExtra": {
      "title": "DELIVERY : D-1",
      "subtitle": "26-08-2020 14:40:30"
    },
    "additional": [
      "FUENTE: INTERNET",
      "CLIENTE: EMERSON ÑAHUINLLA VELASQUEZ",
      "DIRECCIÓN: AV VILLA EL SOL MZ E LT O",
      "CELULAR : 983780014",
      "REFERENCIA : DESVIO DE TIERRA DESPUES DE MECANICA DE MOTOS",
      "PAGARA : 100.00"
    ],
    "items": [
      {
        "quantity": 1,
        "description": "HAWAYANA (FAMILIAR)",
        "commentary": "con arto quesooo",
        "totalPrice": 14.50
      },
      {
        "quantity": 1,
        "description": "HAWAYANA (PERSONAL)",
        "totalPrice": 14.50
      }
    ]
  }
}
```

* Modelo precuentas para restaurante

```json
{
  "type": "precount",
  "times": 1,
  "printer": {
  },
  "data": {
    "business": {
      "description": "Restaurant H. Pollos"
    },
    "document": {
      "description": "PRECUENTA"
    },
    "additional": [
      "FECHA EMISIÓN : 01/10/2019 14:51:26",
      "Mesero(a) : Luis",
      "Mesa : Delivery"
    ],
    "items": [
      {
        "quantity": 1,
        "description": "HAWAYANA (FAMILIAR)",
        "totalPrice": 14.50
      },
      {
        "quantity": 1,
        "description": "HAWAYANA (PERSONAL)",
        "totalPrice": 14.50
      }
    ],
    "amounts": {
      "Total": "25.00"
    }
  }
}
```

* Para notas de venta solo se considerará las propiedades:
  document, customer y additional

```typescript
interface note {
    type: "note";
    //.. demas propiedades
    document: {
        description: string;
        identifier: string;
    }
    customer: string[];
    additional: string[];
}
```

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
    }
    //... las demas propiedaes van aqui
    stringQR:"20450523381|01|F001|00000006|0|9.00|30/09/2019|6|sdfsdfsdf|"
    metadata: {
        logoPath: "C:\\Imagenes\\logo.png"
    }
}
```

