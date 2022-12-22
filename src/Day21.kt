fun main() {
    fun parse(input: List<String>): MutableList<Job> = input.map { Job.parse(it) }.toMutableList()

    fun part1(input: List<String>): Long {
        val jobs = parse(input)

        val monkeys = jobs.associateBy { it.monkey }

        monkeys.values.filterIsInstance<Job.Operate>().forEach { operateJob ->
            operateJob.job1 = monkeys[operateJob.monkey1]!!
            operateJob.job2 = monkeys[operateJob.monkey2]!!
        }

        val root = jobs.first { it.monkey == "root" }
        return root.yell()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("input21")
    println(part1(input))
    println(part2(input))
}

enum class Operation { ADD, SUBTRACT, MULTIPLY, DIVIDE }

sealed interface Job {
    val monkey: String
    fun yell(): Long

    data class Yell(override val monkey: String, val number: Long) : Job {
        override fun yell(): Long = number
    }

    data class Operate(
        override val monkey: String,
        val monkey1: String,
        val monkey2: String,
        val operation: Operation
    ) : Job {
        lateinit var job1: Job
        lateinit var job2: Job
        override fun yell(): Long = when (operation) {
            Operation.ADD -> job1.yell() + job2.yell()
            Operation.SUBTRACT -> job1.yell() - job2.yell()
            Operation.MULTIPLY -> job1.yell() * job2.yell()
            Operation.DIVIDE -> job1.yell() / job2.yell()
        }
    }

    companion object {
        fun parse(input: String): Job {
            val (monkey, job) = input.split(": ")
            return when {
                job.all { it.isDigit() } -> Yell(monkey, job.toLong())
                "+" in job -> {
                    val (monkey1, monkey2) = job.split(" + ")
                    Operate(monkey, monkey1, monkey2, Operation.ADD)
                }

                "-" in job -> {
                    val (monkey1, monkey2) = job.split(" - ")
                    Operate(monkey, monkey1, monkey2, Operation.SUBTRACT)
                }

                "*" in job -> {
                    val (monkey1, monkey2) = job.split(" * ")
                    Operate(monkey, monkey1, monkey2, Operation.MULTIPLY)
                }

                "/" in job -> {
                    val (monkey1, monkey2) = job.split(" / ")
                    Operate(monkey, monkey1, monkey2, Operation.DIVIDE)
                }

                else -> error("Error!")
            }
        }
    }
}
