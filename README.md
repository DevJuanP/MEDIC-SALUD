# Medic Salud

Esta será la estructura del proyecto:
***
pe.edu.cibertec.medicsalud/
│
├── db/                                   # Para guardar datos localmente (Room)
│   ├── dao/
│   │   └── TokenDao.kt                   # Dao para leer/guardar los tokens cifrados
│   ├── entity/
│   │   └── TokenEntity.kt                # Almacena accessToken, refreshToken y expiresAt
│   └── MedicSaludDatabase.kt             # Configuración de la Base de Datos Room
│
├── retrofit/                             # Conexión con la API RxLink
│   ├── api/
│   │   ├── IAuthService.kt               # Endpoints de Login, Register, Refresh y Logout
│   │   ├── IDoctorService.kt             # Endpoints de Especialidades y Médicos
│   │   └── IAppointmentService.kt        # Endpoints de Citas, Diagnósticos y Recetas
│   │
│   ├── request/                          # Datos que ENVIÁS al servidor en los POST/PATCH
│   │   ├── LoginRequest.kt               # json con email y password
│   │   ├── RegisterRequest.kt            # json con los datos del formulario de registro
│   │   ├── AppointmentRequest.kt         # json con availabilityCode y consultationTypeCode
│   │   └── RefreshRequest.kt             # json con el refreshToken
│   │
│   ├── response/                         # Datos que RECIBES del servidor (Mapeo de JSONs)
│   │   ├── AuthResponse.kt               # Trae tokens y datos básicos del paciente
│   │   ├── PatientData.kt                # Sub-objeto del paciente (names, email, etc.)
│   │   ├── SpecialtyResponse.kt          # Mapea specialtyCode, name, doctorCount
│   │   ├── DoctorResponse.kt             # Mapea userCode, names, surnames, licenseNumber
│   │   ├── AvailableDatesResponse.kt     # Mapea las fechas disponibles del médico
│   │   ├── AvailableSlotsResponse.kt     # Mapea los horarios disponibles (`slots`)
│   │   ├── AppointmentResponse.kt        # Datos completos de la cita creada o listada
│   │   ├── DiagnosticResponse.kt         # Mapea la lista de diagnósticos
│   │   └── PrescriptionResponse.kt       # Mapea el detalle completo de la receta
│   │
│   ├── ClientRetrofit.kt                 # Configuración centralizada de Retrofit y OkHttpClient
│   └── TokenAuthenticator.kt             # Interceptor que maneja el error 401 y refresca el token
│
├── util/                                 # Clases de soporte y configuración global
│   └── Constantes.kt                     # Contiene la URL_API_BASE ("http://10.0.2.2:5207/api/")
│
└── view/                                 # Interfaz gráfica de usuario (Pantallas y listas)
    ├── adapter/                          # Controladores para los RecyclerViews
    │   ├── SpecialtyAdapter.kt           # Muestra las tarjetas de especialidades
    │   ├── DoctorAdapter.kt              # Muestra los médicos de una especialidad
    │   ├── SlotAdapter.kt                # Muestra los botones de horas disponibles
    │   ├── AppointmentAdapter.kt         # Muestra el listado de "Mis Citas"
    │   └── DiagnosticAdapter.kt          # Muestra el historial de diagnósticos y recetas
    │
    ├── AuthLoginActivity.kt              # Pantalla para iniciar sesión
    ├── AuthRegisterActivity.kt           # Pantalla con el formulario de registro
    ├── MainActivity.kt                   # Menú principal o Dashboard del Paciente (Citas, Historial, Perfil)
    ├── DoctorDiscoverActivity.kt         # Pantalla para buscar especialidades y médicos
    ├── BookAppointmentActivity.kt        # Pantalla de reserva (Selección de fecha y hora)
    ├── PaymentSimulationActivity.kt      # Pantalla del pago simulado
    └── PrescriptionDetailActivity.kt     # Pantalla para visualizar los medicamentos recetados
***    