# Ejemplos de diseño para Tablas

## Ejemplo 1

- Personalización de cellBodyStyles, para configurar la alineación de las columnas de indice 1 y 2
  que hacen referencia a las columnas 2 y 3 de una tabla
- Personalización de un header en concreto

```json
{
	"printer": {
		"properties": {
			"width": 42
		}
	}
  "tables": [
    {
      "cellBodyStyles": {
        "1": "center",
        "2": "right"
      },
      "title": "CONTADOR DE MONEDAS",
      "headers": [
        "Billete/Moneda",
        "Cantidad",
        {
          "text": "Subtotal",
          "align": "right"
        }
      ],
      "body": [
        ["$ 100.00", "0", "0.00"],
        ["$ 50.00", "0", "100.00"],
        ["$ 20.00", "1", "20.00"],
        ["$ 50.00", "1", "50.00"],
        ["$ 10.00", "1", "10.00"]
      ]
    }
  ]
}
```

## Ejemplo 2

- Definición de los porcentajes de ancho en widthPercents para controlar el comportamiento responsive
  de las tablas.
- Uso de la propiedad span en footer para que un item ocupe dos columnas.

```json
{
  "printer": {
    "properties": {
      "width": 42
    }
  },
  "tables": {
    "title": {
      "text": "CAJA CHICA",
      "decorator": "*"
    },
    "widthPercents": [20, 40],
    "cellBodyStyles": {
      "2": "right"
    },
    "body": [
      ["FECHA:", "", "29-04-2024"],
      ["MONTO:", "", "1000.00"]
    ],
    "footer": [
      {
        "text": "CAJA USUARIO(S):",
        "span": 2
      },
      {
        "text": "ADMIN",
        "align": "right"
      }
    ]
  }
}
```

## Ejemplo número 3

- Agregar titulo general, detalles adicionales y detalles finales
- Elementos del body mixtos, objectos y array de strings.

```json
{
  "title": ["JTICKET DESIGN", "REPORTE DE EJEMPLO"],
  "details": [
    "LUGAR : ventana 01",
    "FECHA - HORA: IMPRESIÓN 30-04-2024",
    "10:41:47"
  ],
  "tables": [
    {
      "widthPercents": [80],
      "cellBodyStyles": {
        "1": "right"
      },
      "title": "Tabla Mixta",
      "body": [
        {
          "text": "Subtitulo 1",
          "bold": true
        },
        ["Item 1", "valor numero 1"],
        [
          {
            "text": "Item 2",
            "align": "right"
          },
          "Valor numero 2"
        ],
        "Subtitulo 2",
        ["Item 3", "Valor numero 3"],
        ["Item 4", "Valor numero 4"],
        [
          {
            "text": "Sumatoria",
            "align": "right"
          },
          "12"
        ]
      ],
      "footer": [
        {
          "text": "Cantidad Total",
          "align": "right"
        },
        {
          "text": "13",
          "align": "right"
        }
      ]
    }
  ],
  "finalDetails": [
    "",
    {
      "text": "---------------------------",
      "bold": true
    },
    "Firma Engargado 1",
    "",
    {
      "text": "---------------------------"
    },
    "Firma Encargado 2"
  ]
}
```

## Ejemplo número 4

- Envio de varias tablas
- Soporte de filas incompletas. (Sin configuración)
- Soporte para encajar una oración en su celda lo mas que se pueda. (Sin configuración)

```json
{
  "printer": {
    "name_system": "{{ name_system }}",
    "port": 9100,
    "type": "{{ type }}",
    "properties": {
      "width": 42
    }
  },
  "title": "Varias tablas a imprimir",
  "tables": [
    {
      "title": "tabla 1",
      "body": [
        ["item 1", "item 2", "item 3"],
        ["item 1", "item 2", "item 3"],
        ["item 1", "item 2", "item 3"],
        ["item 1", "item 2", "item 3"]
      ]
    },
    {
      "title": {
        "text": "tabla 2",
        "decorator": "*"
      },
      "separator": "|",
      "cellBodyStyles": {
        "0": "center"
      },
      "body": [
        ["item 4", "item 5"],
        ["Item 6", "Item 7", "Item 8", "Item 9"],
        ["Item 1"],
        ["", "", "", "Esta es una frase multilinea"]
      ]
    }
  ]
}
```

## ...

- A partir de aqui ya se puede probar todas las propiedades configurables.
Se puede empezar a crear un reporte con un json tan basico como el siguiente.

```json
{
	"printer": {
		"properties": {
			"width": 42
		}
	},
	"tables": [
		{
			"title": "Primera tabla"
		},
		{
			"title": "Segunda tabla"
		}
	]
}
```