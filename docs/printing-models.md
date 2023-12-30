### Ejemplos modelos de impresión
1. [Boleta o factura](#1-modelo-para-boleta-o-factura)
2. [Extras](#2-modelo-para-extras)
3. [Encomienda](#3-modelo-para-encomienda)
4. [Comanda restaurante](#4-comanda-restaurante)
5. [Extras o delivery restaurante](#5-modelo-de-extra-o-delivery-restaurante)
6. [Precuenta restaurante](#6-modelo-precuenta-restaurante)
7. [Notas de venta](#7-notas-de-venta)

#### 1. Modelo para boleta o factura.
* type: invoice:
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
#### 2. Modelo para extras.
* type: invoice, con caracteristicas por defecto:
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
#### 3. Modelo para encomienda.
* type: invoice, con ancho de ticket 30:
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
#### 4. Comanda, restaurante.
* type: command, para una ticketera sin soporte de backgroundInverted
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
#### 5. Modelo de extra o delivery, restaurante.
* type: extra, con carasteristicas por defecto:
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
#### 6. Modelo precuenta, restaurante.
* type: precount
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
#### 7. Notas de venta
* type: note
* Para notas de venta solo se considerará las propiedades:
  document, customer y additional
```typescript
interface note {
    type: "note";
    //.. demas propiedades
    data: {
        document: {
            description: string;
            identifier: string;
        }
        customer: string[];
        additional: string[];
    }
}
```