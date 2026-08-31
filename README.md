# Event Management System 🎟️

A desktop event management and ticket booking application built with Java, Swing, and Oracle SQL. Supports admin and user roles for creating events, booking tickets, processing payments, and generating reports.

## Features

### Admin
- Create, update, and delete events (Workshops, Seminars, Sports Events)
- View all events and users
- Generate reports: event booking report, user registration report, booking report, upcoming events, full-capacity events

### User
- Browse and search events
- Book tickets with duplicate-booking and capacity checks
- Multiple payment methods: Card Payment, JazzCash, Simple Cash
- Cancel bookings
- View booking status
- Update profile (name, email, password)

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| GUI | Java Swing (custom-styled components: AccentButton, DarkTable, GlassPanel, Toast, etc.) |
| Database | Oracle SQL |
| DB Connectivity | JDBC (`oracle.jdbc.driver.OracleDriver`) |
| IDE | Eclipse |

## Architecture

The project follows a layered structure separating models, data access, and UI:

```
Oop Final Project/
├── src/
│   ├── dao/          # Data Access Objects (BookingDAO, EventDAO, UserDAO)
│   ├── db/           # DBConnect — handles JDBC connection
│   ├── main/          # Main.java (entry point), EventManagementGUI.java (Swing UI)
│   └── model/        # Domain classes (Event, Booking, User, Payment, and subclasses)
├── .classpath
├── .project
├── .gitignore
└── db.properties     # DB credentials (not committed — see below)
```

- **Model layer**: `Event` is an abstract base class extended by `Workshop`, `Seminar`, and `SportsEvent`. `Payment` is an abstract base class extended by `CardPayment`, `JazzCash_Payment`, and `SimpleCash_Payment` — a clean use of polymorphism for event categories and payment types.
- **DAO layer**: Each DAO (`BookingDAO`, `EventDAO`, `UserDAO`) handles its own SQL queries via `PreparedStatement`, with explicit `con.commit()` calls required for Oracle JDBC to persist changes.
- **GUI layer**: `EventManagementGUI.java` is the Swing-based interface; `Main.java` launches it via `SwingUtilities.invokeLater()`.

## Getting Started

### Prerequisites
- Java JDK installed
- Eclipse IDE
- Oracle Database (running locally, e.g. via Oracle XE)
- Oracle JDBC driver (`ojdbc` jar) added to your build path

### Installation

```bash
git clone https://github.com/maham146/event-management-system.git
```

Then open the folder in Eclipse: **File → Open Projects from File System** and select the cloned folder.

### Database Setup

You'll need an Oracle database with `users`, `events`, and `bookings` tables matching the fields used in the DAO classes (e.g. `event_name`, `location`, `capacity`, `event_date`, `registration_last_date`, `category`, `price`, `special_name` for events).

### Configure Database Connection

Database credentials are kept out of source code in a `db.properties` file (not committed to the repo). To run the project locally, create a `db.properties` file in the project root with:

```
db.url=jdbc:oracle:thin:@localhost:1521/orcl
db.user=your_username
db.password=your_password
```

`DBConnect.java` reads these values at runtime, so your actual credentials never end up in version control.

### Run the App

Run `Main.java` from Eclipse (Run → Run As → Java Application). This launches the Swing GUI.

## Future Improvements

- Input validation on the GUI (currently minimal client-side validation)
- Email/SMS notifications for booking confirmations
- Export reports to PDF/Excel

## License

This project is for educational purposes.
