import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dermatologist {
    private String name;
    private Map<String, List<LocalTime>> schedule = new HashMap<>(); // Map

    public Dermatologist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addAvailability(String day, String startTime, String endTime) {
        List<LocalTime> slots = generateTimeSlots(startTime, endTime);
        schedule.put(day.toUpperCase(), slots); // Store day in uppercase for consistency
    }

    private List<LocalTime> generateTimeSlots(String start, String end) {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = LocalTime.parse(end);

        while (startTime.isBefore(endTime)) {
            slots.add(startTime);
            startTime = startTime.plusMinutes(15);
        }
        return slots;
    }

    public List<LocalTime> getAvailableSlots(String day) {
        return schedule.getOrDefault(day.toUpperCase(), new ArrayList<>());
    }

    public boolean isSlotAvailable(String day, String time) {
        LocalTime requestedTime = LocalTime.parse(time);
        return schedule.containsKey(day.toUpperCase()) && schedule.get(day.toUpperCase()).contains(requestedTime);
    }

    public void reserveSlot(String day, String time) {
        if (schedule.containsKey(day.toUpperCase())) {
            schedule.get(day.toUpperCase()).remove(LocalTime.parse(time));
        }
    }
}
