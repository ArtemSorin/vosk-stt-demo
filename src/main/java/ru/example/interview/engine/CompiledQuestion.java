package ru.example.interview.engine;

/**
 * Вопрос, «предкомпилированный» при старте: ключевые понятия эталона в виде int-массивов,
 * чтобы разбор ответа во время интервью не делал ни поиска по строкам, ни аллокаций.
 *
 * @param poolIds          id ключевых понятий эталона (в порядке убывания веса)
 * @param poolW            веса понятий (IDF × коэффициенты)
 * @param core             true для «ядра» — самых весомых понятий
 * @param poolIndex        id словаря → индекс в pool или -1
 * @param topic            id словаря → «относится к теме вопроса»
 * @param followUps        уточняющие вопросы с привязкой к понятиям пула
 * @param glossaryInPool   сколько терминов/сленга среди понятий пула
 */
public record CompiledQuestion(Question question, int[] poolIds, double[] poolW, boolean[] core, double poolTotalW,
                               double coreTotalW, int[] poolIndex, boolean[] topic, FollowUp[] followUps,
                               int refWords, int targetWords, int glossaryInPool) {

    public record FollowUp(String text, int[] poolIdx) {}

    public int id() {
        return question.id();
    }

    public Level level() {
        return question.level();
    }

    public int poolSize() {
        return poolIds.length;
    }
}
