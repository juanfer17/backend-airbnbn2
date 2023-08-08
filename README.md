ParkingFlyPass
Descripción
Este repositorio contiene el código del backend de ParkingFlyPass, una aplicación relacionada con la gestión de transacciones de estacionamiento y estadísticas.

Instalación
Este proyecto utiliza Node.js en su versión 18.12.1.

A continuación, se describen los principales métodos implementados en este proyecto:

createTransaction: Crea una nueva transacción de estacionamiento asociada a un comercio y un vendedor.

endTransaction: Finaliza una transacción de estacionamiento. Envía una notificación para marcar la finalización.

averageTransactions: Calcula el promedio de tiempo de transacciones de estacionamiento basado en el tipo de vehículo.

maxTimeService: Consulta y devuelve el tiempo máximo de servicio registrado para transacciones de estacionamiento.
Pruebas Unitarias
Ejecuta npm test para ejecutar las pruebas unitarias utilizando Jasmine.

