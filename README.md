# telma
#### Desktop App for Measurement and Control of Load Stand for Rally Car

[Induction braking][telma] systems dissipate the braking energy by generating eddy currents and as a result the shaft is slowing down. In this stand, these devices are used to create a load torque  and control the speed of the rally car. This is achieved by power electronics, which control the average voltage applied to induction brakes.  
The main goal of stand is to measure the mechanical characteristics (torque-speed characteristics). Based on the obtained data, engineers make a conclusion about the current performance of an internal combustion engine. If the results are unsatisfactory, then there is a change in the engine configuration, for example in the ignition angles, fuel mixture, etc.

Desktop application developed on the **JavaFX** platform. The control and reading of data from the stand is performed by an external microcontroller. The connection is established through a **TCP/IP socket**.

[telma]: <https://www.telma.com/produits/fonctionnement>

![Front view of the car](images/tavria.png)
![GUI](images/gui.png)