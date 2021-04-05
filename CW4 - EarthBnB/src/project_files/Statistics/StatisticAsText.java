package project_files.Statistics;

import javafx.scene.control.Label;

/**
 * Class StatisticAsText - Provides a label for all statistics that only display text.
 * @author Valentin Magis
 * @version 1.0
 * @since 2021-03-11
 */
public abstract class StatisticAsText extends Statistic {
    protected Label statLabel = new Label();

    public  StatisticAsText()
    {
        statistic = statLabel;
    }
}
