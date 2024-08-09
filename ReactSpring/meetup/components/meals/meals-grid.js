// src/components/meals/meals-grid.js
import MealsItem from './meal-item';
import classes from './meals-grid.module.css';

export default function MealsGrid({ meals }) {
    return (
        <ul className={classes.meals}>
            {meals.map(meal => (
                <li key={meal.id}>
                    <MealsItem {...meal} />
                </li>
            ))}
        </ul>
    );
}
