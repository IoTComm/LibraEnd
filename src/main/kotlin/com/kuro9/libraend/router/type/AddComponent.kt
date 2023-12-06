import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

data class NewDesk(val id: Int)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class NewSeat(val seatId: Int, val deskId: Int)