import java.util.PriorityQueue;
import java.util.Scanner;

class Doctor implements Comparable<Doctor> {
    int id;
    int avgConsultationTime;
    int nextAvailableTime;

    public Doctor(int id, int avgConsultationTime, int nextAvailableTime) {
        this.id = id;
        this.avgConsultationTime = avgConsultationTime;
        this.nextAvailableTime = nextAvailableTime;
    }

    @Override
    public int compareTo(Doctor other) {
        return Integer.compare(this.nextAvailableTime, other.nextAvailableTime);
    }
}

public class ClinicQueue {
    public static int calculateWaitingTime(Doctor[] doctors, int patientPosition) {
        PriorityQueue<Doctor> doctorQueue = new PriorityQueue<>();

        // Initialize priority queue with all doctors
        for (Doctor doctor : doctors) {
            doctorQueue.offer(doctor);
        }

        // Assign each patient to the doctor with the shortest waiting time
        int waitingTime = 0;
        for (int i = 0; i < patientPosition; i++) {
            Doctor nextDoctor = doctorQueue.poll();
            waitingTime = Math.max(waitingTime, nextDoctor.nextAvailableTime);
            nextDoctor.nextAvailableTime += nextDoctor.avgConsultationTime;
            doctorQueue.offer(nextDoctor);
        }

        // Get the final waiting time for the patient at the given position
        Doctor finalDoctor = doctorQueue.poll();
        waitingTime = Math.max(waitingTime, finalDoctor.nextAvailableTime);
        return waitingTime;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get the number of doctors
        System.out.print("Enter the number of doctors: ");
        int numDoctors = getPositiveInput(scanner);

        // Get the average consultation time and current remaining time for each doctor
        Doctor[] doctors = new Doctor[numDoctors];
        for (int i = 0; i < numDoctors; i++) {
            System.out.print("Enter the average consultation time for Doctor " + (i + 1) + " (in minutes, > 0): ");
            int avgTime = getPositiveInput(scanner);

            System.out.print("Enter the remaining consultation time for Doctor " + (i + 1) + " with their current patient (0 if not busy): ");
            int remainingTime = getValidInput(scanner);

            doctors[i] = new Doctor(i + 1, avgTime, remainingTime);
        }

        // Get the patient's position in the queue
        System.out.print("Enter the patient's position in the queue (1-based index): ");
        int patientPosition = getValidInput(scanner) - 1; // Convert to 0-based index

        if (patientPosition >= 0) {
            // Calculate the waiting time
            int waitingTime = calculateWaitingTime(doctors, patientPosition);
            System.out.println("Estimated waiting time for the patient: " + waitingTime + " minutes.");
        } else {
            System.out.println("Invalid patient position.");
        }

        scanner.close();
    }

    private static int getValidInput(Scanner scanner) {
        while (true) {
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                if (input >= 0) {
                    return input;
                }
            } else {
                scanner.next(); // Clear invalid input
            }
            System.out.print("Invalid input. Please enter a non-negative integer: ");
        }
    }

    private static int getPositiveInput(Scanner scanner) {
        while (true) {
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                if (input > 0) {
                    return input;
                }
            } else {
                scanner.next(); // Clear invalid input
            }
            System.out.print("Invalid input. Please enter a positive integer: ");
        }
    }
}
