import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.expense.tracker.database.MyDatabase

actual fun createDb(): MyDatabase {
    val driver = NativeSqliteDriver(MyDatabase.Schema, "expensetrackerapp.db")
    return MyDatabase(driver)
}