package com.ruben.dailymotivator

import android.Manifest
import android.app.TimePickerDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ruben.dailymotivator.ui.theme.DailyMotivatorTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.*
import java.util.concurrent.TimeUnit
import java.text.SimpleDateFormat

// --- API Implementation ---
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://api.pexels.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val pexelsService: PexelsService = retrofit.create(PexelsService::class.java)
const val PEXELS_API_KEY = "EsjEasYvtFEZnZqcKJkofzaQKxEqdgfpe506JmHAMGo4kOKISYDhB8dE"

val quoteRetrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://zenquotes.io/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val quoteService: QuoteService = quoteRetrofit.create(QuoteService::class.java)

// --- Static Data ---
val quotes = listOf(
    Quote("Единственият начин да вършиш страхотна работа е да обичаш това, което правиш.", "Стив Джобс", "BG"),
    Quote("The only way to do great work is to love what you do.", "Steve Jobs", "EN"),
    Quote("Вярвай, че можеш, и си изминал половината път.", "Теодор Рузвелт", "BG"),
    Quote("Believe you can and you're halfway there.", "Theodore Roosevelt", "EN"),
    Quote("Бъди промяната, която искаш да видиш в света.", "Махатма Ганди", "BG"),
    Quote("Be the change that you wish to see in the world.", "Mahatma Gandhi", "EN"),
    Quote("Вашето време е ограничено, така че не го губете, живеейки нечий друг живот.", "Стив Джобс", "BG"),
    Quote("Your time is limited, so don't waste it living someone else's life.", "Steve Jobs", "EN"),
    Quote("Ако искате да живеете щастлив живот, вържете го към цел, а не към хора или вещи.", "Алберт Айнщайн", "BG"),
    Quote("If you want to live a happy life, tie it to a goal, not to people or things.", "Albert Einstein", "EN"),
    Quote("Успехът не е ключът към щастието. Щастието е ключът към успеха.", "Алберт Швайцер", "BG"),
    Quote("Success is not the key to happiness. Happiness is the key to success.", "Albert Schweitzer", "EN"),
    Quote("Най-голямата ни слабост се крие в това да се откажем.", "Томас Едисон", "BG"),
    Quote("Our greatest weakness lies in giving up.", "Thomas Edison", "EN"),
    Quote("Не се страхувай да се откажеш от доброто, за да търсиш великото.", "Джон Д. Рокфелер", "BG"),
    Quote("Don't be afraid to give up the good to go for the great.", "John D. Rockefeller", "EN"),
    Quote("Трудностите често подготвят обикновените хора за необикновена съдба.", "К. С. Луис", "BG"),
    Quote("Hardships often prepare ordinary people for an extraordinary destiny.", "C.S. Lewis", "EN"),
    Quote("Единственото нещо, което стои между теб и твоята мечта, е волята да опиташ и вярата, че е възможно.", "Джоел Браун", "BG"),
    Quote("The only thing standing between you and your dream is the will to try and the belief that it is actually possible.", "Joel Brown", "EN"),
    Quote("Никога не поглеждай назад, освен ако нямаш намерение да вървиш в тази посока.", "Хенри Дейвид Торо", "BG"),
    Quote("Never look back unless you are planning to go that way.", "Henry David Thoreau", "EN"),
    Quote("Бъдещето принадлежи на тези, които вярват в красотата на своите мечти.", "Елинор Рузвелт", "BG"),
    Quote("The future belongs to those who believe in the beauty of their dreams.", "Eleanor Roosevelt", "EN"),
    Quote("Не преброявай дните, направи така, че дните да се броят.", "Мохамед Али", "BG"),
    Quote("Don't count the days, make the days count.", "Muhammad Ali", "EN"),
    Quote("Успехът е да вървиш от провал към провал, без да губиш ентусиазъм.", "Уинстън Чърчил", "BG"),
    Quote("Success is stumbling from failure to failure with no loss of enthusiasm.", "Winston Churchill", "EN"),
    Quote("Колкото по-трудна е битката, толкова по-сладка е победата.", "Лес Браун", "BG"),
    Quote("The harder the battle, the sweeter the victory.", "Les Brown", "EN"),
    Quote("Всички наши мечти могат да се сбъднат, ако имаме смелостта да ги преследваме.", "Уолт Дисни", "BG"),
    Quote("All our dreams can come true, if we have the courage to pursue them.", "Walt Disney", "EN"),
    Quote("В средата на трудността лежи възможността.", "Алберт Айнщайн", "BG"),
    Quote("In the middle of difficulty lies opportunity.", "Albert Einstein", "EN"),
    Quote("Не позволявай на вчерашния ден да отнема твърде много от днешния.", "Уил Роджърс", "BG"),
    Quote("Don't let yesterday take up too much of today.", "Will Rogers", "EN"),
    Quote("Лошата новина е, че времето лети. Добрата новина е, че ти си пилотът.", "Майкъл Алтшулер", "BG"),
    Quote("The bad news is time flies. The good news is you're the pilot.", "Michael Altshuler", "EN"),
    Quote("За да започнеш, трябва да спреш да говориш и да започнеш да правиш.", "Уолт Дисни", "BG"),
    Quote("The way to get started is to quit talking and begin doing.", "Walt Disney", "EN"),
    Quote("Ограниченията съществуват само в нашето съзнание.", "Наполеон Хил", "BG"),
    Quote("The only limits that exist are the ones in our minds.", "Napoleon Hill", "EN"),
    Quote("Всяко постижение започва с решението да опиташ.", "Джон Кенеди", "BG"),
    Quote("Every accomplishment starts with the decision to try.", "John F. Kennedy", "EN"),
    Quote("Успехът обикновено идва при тези, които са твърде заети, за да го търсят.", "Хенри Дейвид Торо", "BG"),
    Quote("Success usually comes to those who are too busy to be looking for it.", "Henry David Thoreau", "EN"),
    Quote("Не чакай. Времето никога няма да бъде точното.", "Наполеон Хил", "BG"),
    Quote("Don't wait. The time will never be just right.", "Napoleon Hill", "EN"),
    Quote("Най-добрият начин да предскажеш бъдещето е да го създадеш.", "Питър Дракър", "BG"),
    Quote("The best way to predict the future is to create it.", "Peter Drucker", "EN"),
    Quote("Животът е 10% това, което ти се случва, и 90% как реагираш на него.", "Чарлз Суиндъл", "BG"),
    Quote("Life is 10% what happens to us and 90% how we react to it.", "Charles R. Swindoll", "EN"),
    Quote("Ако не изградиш мечтата си, някой друг ще те наеме, за да му помогнеш да изгради неговата.", "Тони Гаскинс", "BG"),
    Quote("If you don't build your dream, someone else will hire you to help them build theirs.", "Tony Gaskins", "EN"),
    Quote("Дисциплината е мостът между целите и постиженията.", "Джим Рон", "BG"),
    Quote("Discipline is the bridge between goals and accomplishment.", "Jim Rohn", "EN"),
    Quote("Бъдете толкова добри, че да не могат да ви игнорират.", "Стив Мартин", "BG"),
    Quote("Be so good they can't ignore you.", "Steve Martin", "EN"),
    Quote("Единствената граница за нашите постижения утре са нашите съмнения днес.", "Франклин Д. Рузвелт", "BG"),
    Quote("The only limit to our realization of tomorrow will be our doubts of today.", "Franklin D. Roosevelt", "EN"),
    Quote("Не върви там, където пътеката може да те отведе, върви там, където няма пътека и остави следа.", "Ралф Уолдо Емерсън", "BG"),
    Quote("Do not go where the path may lead, go instead where there is no path and leave a trail.", "Ralph Waldo Emerson", "EN"),
    Quote("Нищо велико не е било постигнато без ентусиазъм.", "Ралф Уолдо Емерсън", "BG"),
    Quote("Nothing great was ever achieved without enthusiasm.", "Ralph Waldo Emerson", "EN"),
    Quote("Човек, който никога не е правил грешка, никога не е опитвал нищо ново.", "Алберт Айнщайн", "BG"),
    Quote("A person who never made a mistake never tried anything new.", "Albert Einstein", "EN"),
    Quote("Твоето образование е превъзходство, но твоят характер е съдба.", "Майкъл Джоунс", "BG"),
    Quote("Your education is a dress rehearsal for a life that is yours to lead.", "Nora Ephron", "EN"),
    Quote("Трябва да бъдеш промяната, която искаш да видиш в света.", "Махатма Ганди", "BG"),
    Quote("Be the change that you wish to see in the world.", "Mahatma Gandhi", "EN"),
    Quote("Единственото нещо, от което трябва да се страхуваме, е самият страх.", "Франклин Д. Рузвелт", "BG"),
    Quote("The only thing we have to fear is fear itself.", "Franklin D. Roosevelt", "EN"),
    Quote("Мечтайте така, сякаш ще живеете вечно. Живейте така, сякаш ще умрете днес.", "Джеймс Дийн", "BG"),
    Quote("Dream as if you'll live forever. Live as if you'll die today.", "James Dean", "EN"),
    Quote("Провалът е просто възможност да започнеш отново, този път по-интелигентно.", "Хенри Форд", "BG"),
    Quote("Failure is simply the opportunity to begin again, this time more intelligently.", "Henry Ford", "EN"),
    Quote("Всичко, което можете да си представите, е реално.", "Пабло Пикасо", "BG"),
    Quote("Everything you can imagine is real.", "Pablo Picasso", "EN"),
    Quote("Не е важно колко бавно вървиш, стига да не спираш.", "Конфуций", "BG"),
    Quote("It does not matter how slowly you go as long as you do not stop.", "Confucius", "EN"),
    Quote("Ако минеш през ада, продължавай да вървиш.", "Уинстън Чърчил", "BG"),
    Quote("If you are going through hell, keep going.", "Winston Churchill", "EN"),
    Quote("Качеството не е акт, то е навик.", "Аристотел", "BG"),
    Quote("Quality is not an act, it is a habit.", "Aristotle", "EN"),
    Quote("Единственият начин да се справиш със страха е да се изправиш срещу него.", "Ралф Уолдо Емерсън", "BG"),
    Quote("The only way to handle fear is to face it.", "Ralph Waldo Emerson", "EN"),
    Quote("Големите умове обсъждат идеи; средните умове обсъждат събития; малките умове обсъждат хора.", "Елинор Рузвелт", "BG"),
    Quote("Great minds discuss ideas; average minds discuss events; small minds discuss people.", "Eleanor Roosevelt", "EN"),
    Quote("Успехът се състои в това да обичаш себе си, да обичаш това, което правиш, и да обичаш начина, по който го правиш.", "Мая Анджелоу", "BG"),
    Quote("Success is liking yourself, liking what you do, and liking how you do it.", "Maya Angelou", "EN"),
    Quote("Ако искате да полетите, трябва да се откажете от нещата, които ви теглят надолу.", "Тони Морисън", "BG"),
    Quote("If you want to fly, you have to give up the things that weigh you down.", "Toni Morrison", "EN"),
    Quote("Никога не е твърде късно да бъдеш това, което е можело да бъдеш.", "Джордж Елиът", "BG"),
    Quote("It is never too late to be what you might have been.", "George Eliot", "EN"),
    Quote("Бъдете себе си; всички останали са вече заети.", "Оскар Уайлд", "BG"),
    Quote("Be yourself; everyone else is already taken.", "Oscar Wilde", "EN"),
    Quote("Днешният ден е единственият, който имаш. Използвай го.", "Опра Уинфри", "BG"),
    Quote("Today is the only day you have. Use it.", "Oprah Winfrey", "EN"),
    Quote("Не чакайте бурята да премине, научете се да танцувате в дъжда.", "Вивиан Грийн", "BG"),
    Quote("Life isn't about waiting for the storm to pass, it's about learning to dance in the rain.", "Vivian Greene", "EN"),
    Quote("Единственият човек, с когото трябва да се сравнявате, сте вие самите в миналото.", "Зигмунд Фройд", "BG"),
    Quote("The only person you should compare yourself to is the person you were yesterday.", "Sigmund Freud", "EN")
)

val phrases = listOf(
    Phrase("Ти си по-силен, отколкото си мислиш!", "BG"),
    Phrase("You are stronger than you think!", "EN"),
    Phrase("Всяка малка стъпка води към голям успех.", "BG"),
    Phrase("Every small step leads to big success.", "EN"),
    Phrase("Не спирай, докато не се гордееш със себе си.", "BG"),
    Phrase("Don't stop until you're proud.", "EN"),
    Phrase("Твоята единствена граница е твоето съзнание.", "BG"),
    Phrase("Your only limit is your mind.", "EN"),
    Phrase("Прави каквото трябва, докато можеш да правиш каквото искаш.", "BG"),
    Phrase("Do what you have to do until you can do what you want to do.", "EN"),
    Phrase("Успехът е сбор от малки усилия, повтаряни ден след ден.", "BG"),
    Phrase("Success is the sum of small efforts, repeated day in and day out.", "EN"),
    Phrase("Вярвай в себе си и всичко е възможно.", "BG"),
    Phrase("Believe in yourself and anything is possible.", "EN"),
    Phrase("Провали се седем пъти, изправи се осем.", "BG"),
    Phrase("Fall seven times, stand up eight.", "EN"),
    Phrase("Дисциплината е изборът между това, което искаш сега, и това, което искаш най-много.", "BG"),
    Phrase("Discipline is choosing between what you want now and what you want most.", "EN"),
    Phrase("Трудните пътища често водят до красиви дестинации.", "BG"),
    Phrase("Difficult roads often lead to beautiful destinations.", "EN"),
    Phrase("Не чакай възможности. Създавай ги.", "BG"),
    Phrase("Don't wait for opportunity. Create it.", "EN"),
    Phrase("Бъди толкова добър, че да не могат да те игнорират.", "BG"),
    Phrase("Be so good they can't ignore you.", "EN"),
    Phrase("Всичко, което някога си искал, е от другата страна на страха.", "BG"),
    Phrase("Everything you've ever wanted is on the other side of fear.", "EN"),
    Phrase("Мечтай мащабно. Работи здраво.", "BG"),
    Phrase("Dream big. Work hard.", "EN"),
    Phrase("Щастието не е нещо готово. То идва от твоите собствени действия.", "BG"),
    Phrase("Happiness is not something readymade. It comes from your own actions.", "EN"),
    Phrase("Ти не намираш щастлив живот. Ти го създаваш.", "BG"),
    Phrase("You don't find a happy life. You make it.", "EN"),
    Phrase("Днешните трудности развиват силата ти за утре.", "BG"),
    Phrase("Today's struggles are developing your strengths for tomorrow.", "EN"),
    Phrase("Никога не е твърде късно да бъдеш това, което е можело да бъдеш.", "BG"),
    Phrase("It is never too late to be what you might have been.", "EN"),
    Phrase("Единственият начин да се провалиш е да се откажеш.", "BG"),
    Phrase("The only way to fail is to quit.", "EN"),
    Phrase("Бъди глас, а не ехо.", "BG"),
    Phrase("Be a voice, not an echo.", "EN"),
    Phrase("Твоят потенциал е безкраен.", "BG"),
    Phrase("Your potential is endless.", "EN"),
    Phrase("Действай така, сякаш това, което правиш, има значение. Защото има.", "BG"),
    Phrase("Act as if what you do makes a difference. It does.", "EN"),
    Phrase("Успехът не е окончателен, провалът не е фатален: важна е смелостта да продължиш.", "BG"),
    Phrase("Success is not final, failure is not fatal: it is the courage to continue that counts.", "EN"),
    Phrase("Не преброявай дните, направи така, че дните да се броят.", "BG"),
    Phrase("Don't count the days, make the days count.", "EN"),
    Phrase("Започни там, където си. Използвай това, което имаш. Прави каквото можеш.", "BG"),
    Phrase("Start where you are. Use what you have. Do what you can.", "EN"),
    Phrase("Единственият човек, с когото трябва да се опитваш да бъдеш по-добър, е човекът, който беше вчера.", "BG"),
    Phrase("The only person you should try to be better than is the person you were yesterday.", "EN"),
    Phrase("Ако искаш да летиш, трябва да се откажеш от нещата, които те теглят надолу.", "BG"),
    Phrase("If you want to fly, you have to give up the things that weigh you down.", "EN"),
    Phrase("Не съжалявай за миналото, учи се от него.", "BG"),
    Phrase("Don't regret the past, learn from it.", "EN"),
    Phrase("Големите неща никога не идват от зоните на комфорт.", "BG"),
    Phrase("Great things never came from comfort zones.", "EN"),
    Phrase("Твоят живот е твоето платно. Нарисувай го красиво.", "BG"),
    Phrase("Your life is your canvas. Paint it beautiful.", "EN"),
    Phrase("Фокусирай се върху целта, а не върху препятствията.", "BG"),
    Phrase("Focus on the goal, not the obstacles.", "EN"),
    Phrase("Бъди по-силен от оправданията си.", "BG"),
    Phrase("Be stronger than your excuses.", "EN"),
    Phrase("Най-доброто време за ново начало е сега.", "BG"),
    Phrase("The best time for a new beginning is now.", "EN"),
    Phrase("Малките навици носят големи резултати.", "BG"),
    Phrase("Small habits lead to big results.", "EN"),
    Phrase("Вярвай в магията на новото начало.", "BG"),
    Phrase("Believe in the magic of new beginnings.", "EN"),
    Phrase("Ти си архитектът на собственото си бъдеще.", "BG"),
    Phrase("You are the architect of your own future.", "EN"),
    Phrase("Всеки ден е нова възможност да станеш по-добър.", "BG"),
    Phrase("Every day is a new chance to get better.", "EN"),
    Phrase("Успехът е пътуване, а не дестинация.", "BG"),
    Phrase("Success is a journey, not a destination.", "EN"),
    Phrase("Остани позитивен, работи здраво и го постигни.", "BG"),
    Phrase("Stay positive, work hard, make it happen.", "EN"),
    Phrase("Ти си достатъчен.", "BG"),
    Phrase("You are enough.", "EN"),
    Phrase("Помни защо започна.", "BG"),
    Phrase("Remember why you started.", "EN"),
    Phrase("Направи го днес, за да ти бъде благодарен утрешният ти аз.", "BG"),
    Phrase("Do it today, so your future self will thank you.", "EN"),
    Phrase("Превърни пречките в трамплин към успеха.", "BG"),
    Phrase("Turn obstacles into stepping stones.", "EN"),
    Phrase("Мисли позитивно и ще привлечеш позитивност.", "BG"),
    Phrase("Think positive and you will attract positivity.", "EN"),
    Phrase("Бъди причината някой да се усмихне днес.", "BG"),
    Phrase("Be the reason someone smiles today.", "EN"),
    Phrase("Никога не се отказвай от мечтите си.", "BG"),
    Phrase("Never give up on your dreams.", "EN"),
    Phrase("Ти контролираш собственото си щастие.", "BG"),
    Phrase("You are in control of your own happiness.", "EN"),
    Phrase("Учи се от вчера, живей за днес, надявай се за утре.", "BG"),
    Phrase("Learn from yesterday, live for today, hope for tomorrow.", "EN"),
    Phrase("Единственото ограничение е това, което сам си поставиш.", "BG"),
    Phrase("The only limit is the one you set yourself.", "EN"),
    Phrase("Твоето време е ценно. Използвай го мъдро.", "BG"),
    Phrase("Your time is precious. Use it wisely.", "EN"),
    Phrase("Бъди най-добрата версия на себе си.", "BG"),
    Phrase("Be the best version of yourself.", "EN"),
    Phrase("Успехът започва с решението да опиташ.", "BG"),
    Phrase("Success starts with the decision to try.", "EN"),
    Phrase("Всеки край е ново начало.", "BG"),
    Phrase("Every end is a new beginning.", "EN")
)

val facts = listOf(
    Fact("Медът е единствената храна, която никога не се разваля.", "BG"),
    Fact("Honey is the only food that never spoils.", "EN"),
    Fact("Октоподите имат три сърца.", "BG"),
    Fact("Octopuses have three hearts.", "EN"),
    Fact("Айфеловата кула може да бъде с 15 см по-висока през лятото.", "BG"),
    Fact("The Eiffel Tower can be 15 cm taller during the summer.", "EN"),
    Fact("Сърцето на синия кит е толкова голямо, че човек може да плува в артериите му.", "BG"),
    Fact("A blue whale's heart is so big, a human can swim through its arteries.", "EN"),
    Fact("Венера е единствената планета, която се върти по посока на часовниковата стрелка.", "BG"),
    Fact("Venus is the only planet that rotates clockwise.", "EN"),
    Fact("Светкавицата е пет пъти по-гореща от повърхността на Слънцето.", "BG"),
    Fact("A lightning bolt is five times hotter than the surface of the sun.", "EN"),
    Fact("Плуто не е направил дори една пълна обиколка около Слънцето, откакто е открит.", "BG"),
    Fact("Pluto hasn't even completed one full orbit around the Sun since it was discovered.", "EN"),
    Fact("Морските кончета са единствените животни, при които мъжкият ражда.", "BG"),
    Fact("Seahorses are the only animals where the male gives birth.", "EN"),
    Fact("Ягодите не са истински плодове, но бананите са.", "BG"),
    Fact("Strawberries are not actual berries, but bananas are.", "EN"),
    Fact("Невъзможно е да кихнете с отворени очи.", "BG"),
    Fact("It is impossible to sneeze with your eyes open.", "EN"),
    Fact("Ленивците могат да задържат дъха си по-дълго от делфините.", "BG"),
    Fact("Sloths can hold their breath longer than dolphins.", "EN"),
    Fact("Кравите имат най-добри приятели и се стресират, когато са разделени.", "BG"),
    Fact("Cows have best friends and get stressed when separated.", "EN"),
    Fact("Мравките никога не спят и нямат бели дробове.", "BG"),
    Fact("Ants never sleep and don't have lungs.", "EN"),
    Fact("На Сатурн и Юпитер вали дъжд от диаманти.", "BG"),
    Fact("It rains diamonds on Saturn and Jupiter.", "EN"),
    Fact("Езикът на синия кит тежи колкото цял слон.", "BG"),
    Fact("A blue whale's tongue weighs as much as an entire elephant.", "EN"),
    Fact("Кенгурутата не могат да ходят назад.", "BG"),
    Fact("Kangaroos cannot walk backwards.", "EN"),
    Fact("Някои костенурки могат да дишат през задните си части.", "BG"),
    Fact("Some turtles can breathe through their butts.", "EN"),
    Fact("Първият портокал не е бил оранжев, а зелен.", "BG"),
    Fact("The first orange was not orange, it was green.", "EN"),
    Fact("Повече хора са убити от крави, отколкото от акули всяка година.", "BG"),
    Fact("More people are killed by cows than sharks every year.", "EN"),
    Fact("Човешкият нос може да запомни 50 000 различни миризми.", "BG"),
    Fact("The human nose can remember 50,000 different scents.", "EN"),
    Fact("Очите на щрауса са по-големи от мозъка му.", "BG"),
    Fact("An ostrich's eye is bigger than its brain.", "EN"),
    Fact("Езикът на жирафа е черен и дълъг около 50 см.", "BG"),
    Fact("A giraffe's tongue is black and about 50 cm long.", "EN"),
    Fact("Облаците могат да тежат милиони килограми.", "BG"),
    Fact("Clouds can weigh millions of pounds.", "EN"),
    Fact("Алфред Нобел е изобретил динамита.", "BG"),
    Fact("Alfred Nobel invented dynamite.", "EN"),
    Fact("Скорпионите светят под ултравиолетова светлина.", "BG"),
    Fact("Scorpions glow under ultraviolet light.", "EN"),
    Fact("Леонардо да Винчи е можел да пише с едната ръка и да рисува с другата едновременно.", "BG"),
    Fact("Leonardo da Vinci could write with one hand and draw with the other at the same time.", "EN"),
    Fact("Бананите са радиоактивни.", "BG"),
    Fact("Bananas are radioactive.", "EN"),
    Fact("В Космоса е невъзможно да се оригнете.", "BG"),
    Fact("It is impossible to burp in space.", "EN"),
    Fact("Кучетата имат уникални отпечатъци на носа, подобно на човешките пръстови отпечатъци.", "BG"),
    Fact("Dogs have unique nose prints, just like human fingerprints.", "EN"),
    Fact("Фламингото става розово заради храната, която яде (скариди).", "BG"),
    Fact("Flamingos turn pink because of the food they eat (shrimp).", "EN"),
    Fact("Прасетата не могат да гледат нагоре към небето.", "BG"),
    Fact("Pigs cannot look up into the sky.", "EN"),
    Fact("Някои охлюви могат да спят в продължение на три години.", "BG"),
    Fact("Some snails can sleep for three years.", "EN"),
    Fact("Пандите прекарват по 12 часа на ден в ядене на бамбук.", "BG"),
    Fact("Pandas spend 12 hours a day eating bamboo.", "EN"),
    Fact("Сърцето на скаридата се намира в главата ѝ.", "BG"),
    Fact("A shrimp's heart is in its head.", "EN"),
    Fact("Белите мечки имат черна кожа под козината си.", "BG"),
    Fact("Polar bears have black skin under their fur.", "EN"),
    Fact("Пеперудите вкусят храната с краката си.", "BG"),
    Fact("Butterflies taste food with their feet.", "EN"),
    Fact("Змиите могат да предсказват земетресения.", "BG"),
    Fact("Snakes can predict earthquakes.", "EN"),
    Fact("Човешкият скелет се обновява напълно на всеки 10 години.", "BG"),
    Fact("The human skeleton renews itself every 10 years.", "EN"),
    Fact("Една чаена лъжичка неутронна звезда би тежала 6 милиарда тона.", "BG"),
    Fact("A teaspoon of a neutron star would weigh 6 billion tons.", "EN"),
    Fact("Крокодилите не могат да си извадят езика.", "BG"),
    Fact("Crocodiles cannot stick their tongues out.", "EN"),
    Fact("В света има повече пластмасови фламингота, отколкото истински.", "BG"),
    Fact("There are more plastic flamingos in the world than real ones.", "EN"),
    Fact("Човек прекарва около 6 месеца от живота си в чакане на червен светофар.", "BG"),
    Fact("The average person spends 6 months of their life waiting for red lights to turn green.", "EN"),
    Fact("Колибрито е единствената птица, която може да лети назад.", "BG"),
    Fact("The hummingbird is the only bird that can fly backwards.", "EN"),
    Fact("Първият компютър е тежал над 27 тона.", "BG"),
    Fact("The first computer weighed more than 27 tons.", "EN"),
    Fact("Делфините спят с едно отворено око.", "BG"),
    Fact("Dolphins sleep with one eye open.", "EN"),
    Fact("Котките имат 32 мускула във всяко ухо.", "BG"),
    Fact("Cats have 32 muscles in each ear.", "EN"),
    Fact("Кока-кола първоначално е била зелена.", "BG"),
    Fact("Coca-Cola was originally green.", "EN"),
    Fact("Слонът е единственото животно, което не може да скача.", "BG"),
    Fact("The elephant is the only animal that cannot jump.", "EN"),
    Fact("В Швеция има хотел, направен изцяло от лед, който се строи наново всяка година.", "BG"),
    Fact("In Sweden, there is a hotel made entirely of ice that is rebuilt every year.", "EN"),
    Fact("Едно дърво може да произведе достатъчно кислород за двама души годишно.", "BG"),
    Fact("One tree can produce enough oxygen for two people per year.", "EN"),
    Fact("Човешкият мозък генерира достатъчно енергия, за да запали малка електрическа крушка.", "BG"),
    Fact("The human brain generates enough electricity to power a small light bulb.", "EN"),
    Fact("Тигрите имат раирана кожа, а не само раирана козина.", "BG"),
    Fact("Tigers have striped skin, not just striped fur.", "EN"),
    Fact("Първото видео в YouTube е качено на 23 април 2005 г.", "BG"),
    Fact("The first YouTube video was uploaded on April 23, 2005.", "EN"),
    Fact("Най-късата война в историята е продължила само 38 минути.", "BG"),
    Fact("The shortest war in history lasted only 38 minutes.", "EN"),
    Fact("Акулите съществуват отпреди дърветата.", "BG"),
    Fact("Sharks have been around longer than trees.", "EN")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Request Notifications Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val requestPermissionLauncher: ActivityResultLauncher<String> = 
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { }
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Schedule Midnight Reset
        ResetWorker.schedule(this)

        val prefs = getSharedPreferences("daily_prefs", MODE_PRIVATE)
        val savedTheme = prefs.getBoolean("dark_mode_key", false)

        setContent {
            var isDarkMode by remember { mutableStateOf(savedTheme) }
            DailyMotivatorTheme(darkTheme = isDarkMode) {
                DailyMotivatorApp(isDarkMode, onThemeToggle = { 
                    isDarkMode = it 
                    prefs.edit { putBoolean("dark_mode_key", it) }
                })
            }
        }
    }
}

@Composable
fun DailyMotivatorApp(isDarkMode: Boolean, onThemeToggle: (Boolean) -> Unit, vm: MotivatorViewModel = viewModel()) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var language by remember { mutableStateOf(Language.BG) }
    val context = LocalContext.current
    
    val habits by vm.allHabits.collectAsState(initial = emptyList())
    val activeMissions by vm.activeMissions.collectAsState(initial = emptyList())
    val completedMissions by vm.completedMissions.collectAsState(initial = emptyList())
    val favorites by vm.allFavorites.collectAsState(initial = emptyList())

    val prefs = remember { context.getSharedPreferences("daily_prefs", Context.MODE_PRIVATE) }
    var streak by remember { mutableIntStateOf(prefs.getInt("streak_key", 0)) }
    var lastDate by remember { mutableStateOf(prefs.getString("last_date_key", "") ?: "") }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val screens = listOf(Screen.Home, Screen.Favorites, Screen.Settings)
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.getTitle(language)) },
                        label = { Text(screen.getTitle(language)) },
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                is Screen.Home -> HomeScreen(
                    language = language,
                    favorites = favorites,
                    onToggleFavorite = { item ->
                        val existing = favorites.find { it.contentText == item.contentText }
                        if (existing != null) vm.removeFavorite(item.contentText)
                        else vm.addFavorite(item, language.name)
                    },
                    habits = habits,
                    onAddHabit = { en, bg -> vm.addHabit(en, bg) },
                    onUpdateHabit = { vm.updateHabit(it) },
                    onDeleteHabit = { vm.deleteHabit(it) },
                    streak = streak,
                    lastDate = lastDate,
                    onStreakUpdate = { newStreak, date ->
                        streak = newStreak
                        lastDate = date
                        prefs.edit { 
                            putInt("streak_key", newStreak)
                            putString("last_date_key", date)
                        }
                    },
                    activeMissions = activeMissions,
                    completedMissions = completedMissions,
                    onAddMission = { text, time -> 
                        vm.addMission(text, time)
                        NotificationHelper.scheduleReminder(context, DailyMission(textBg = text, textEn = text, reminderTime = time))
                    },
                    onCompleteMission = { vm.completeMission(it) },
                    onRecordProgress = { vm.recordProgress(it) }
                )
                is Screen.Favorites -> FavoritesScreen(language, favorites, onRemove = { vm.removeFavorite(it) })
                is Screen.Settings -> SettingsScreen(
                    language = language,
                    onLanguageChange = { language = it },
                    isDarkMode = isDarkMode,
                    onThemeToggle = onThemeToggle
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    language: Language,
    favorites: List<FavoriteItem>,
    onToggleFavorite: (Favoriteable) -> Unit,
    habits: List<Habit>,
    onAddHabit: (String, String) -> Unit,
    onUpdateHabit: (Habit) -> Unit,
    onDeleteHabit: (Habit) -> Unit,
    streak: Int,
    lastDate: String,
    onStreakUpdate: (Int, String) -> Unit,
    activeMissions: List<DailyMission>,
    completedMissions: List<DailyMission>,
    onAddMission: (String, String?) -> Unit,
    onCompleteMission: (DailyMission) -> Unit,
    onRecordProgress: (Int) -> Unit,
    vm: MotivatorViewModel = viewModel()
) {
    val context = LocalContext.current
    
    // Observe state from ViewModel
    val currentQuote by vm.currentQuote.collectAsState()
    val currentPhrase by vm.currentPhrase.collectAsState()
    val currentFact by vm.currentFact.collectAsState()
    val backgroundImage by vm.backgroundImage.collectAsState()

    val scope = rememberCoroutineScope()
    
    // History to prevent repetition (tracking last 20 items)
    var shownQuotes by remember { mutableStateOf(listOf<String>()) }
    var shownPhrases by remember { mutableStateOf(listOf<String>()) }
    var shownFacts by remember { mutableStateOf(listOf<String>()) }

    var party by remember { mutableStateOf<List<Party>>(emptyList()) }
    var isDayFinished by remember { mutableStateOf(false) }

    fun updateContent() {
        // --- Phrase Shuffle (Don't repeat last 20) ---
        val availablePhrases = phrases.filter { it.lang == language.name && it.text !in shownPhrases }
        val nextPhrase = if (availablePhrases.isNotEmpty()) availablePhrases.random() else {
            phrases.filter { it.lang == language.name }.random()
        }
        vm.updatePhrase(nextPhrase)
        shownPhrases = (shownPhrases + nextPhrase.text).takeLast(20)

        // --- Fact Shuffle (Don't repeat last 20) ---
        val availableFacts = facts.filter { it.lang == language.name && it.text !in shownFacts }
        val nextFact = if (availableFacts.isNotEmpty()) availableFacts.random() else {
            facts.filter { it.lang == language.name }.random()
        }
        vm.updateFact(nextFact)
        shownFacts = (shownFacts + nextFact.text).takeLast(20)

        scope.launch {
            try {
                // Fetch Image
                val response = pexelsService.searchPhotos(PEXELS_API_KEY, "nature landscape", 15)
                if (response.photos.isNotEmpty()) {
                    val urls = response.photos.map { it.src.large2x }
                    val nextUrl = urls.random()
                    vm.updateBackgroundImage(nextUrl)
                    vm.cacheImages(urls)
                }
                
                // Fetch Quote
                if (language == Language.EN) {
                    val apiQuotes = quoteService.getRandomQuote()
                    if (apiQuotes.isNotEmpty()) {
                        val newQuote = Quote(apiQuotes[0].q, apiQuotes[0].a, "EN")
                        vm.updateQuote(newQuote)
                        vm.cacheQuotes(listOf(newQuote))
                    }
                } else {
                    // --- Bulgarian Quote Shuffle ---
                    val availableQuotes = quotes.filter { it.lang == "BG" && it.contentText !in shownQuotes }
                    val nextQuote = if (availableQuotes.isNotEmpty()) availableQuotes.random() else {
                        quotes.filter { it.lang == "BG" }.random()
                    }
                    vm.updateQuote(nextQuote)
                    shownQuotes = (shownQuotes + nextQuote.contentText).takeLast(20)
                }
            } catch (_: Exception) {
                // Offline Fallback with Shuffle
                val cachedUrls = vm.getOfflineImages()
                if (cachedUrls.isNotEmpty()) vm.updateBackgroundImage(cachedUrls.random())

                val cachedQuotes = vm.getOfflineQuotes(language.name)
                val finalQuotes = cachedQuotes.ifEmpty { quotes.filter { it.lang == language.name } }
                
                val availableQuotes = finalQuotes.filter { it.contentText !in shownQuotes }
                val nextQuote = if (availableQuotes.isNotEmpty()) availableQuotes.random() else {
                    finalQuotes.random()
                }
                vm.updateQuote(nextQuote)
                shownQuotes = (shownQuotes + nextQuote.contentText).takeLast(20)
            }
        }
    }

    // Only update if current content is null (first run)
    LaunchedEffect(Unit) { 
        if (currentQuote == null || currentPhrase == null || currentFact == null) {
            updateContent() 
        }
    }
    
    val allDone = habits.isNotEmpty() && habits.all { it.isChecked }
    
    LaunchedEffect(allDone) {
        if (allDone && !isDayFinished) {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            if (lastDate != today) {
                isDayFinished = true
                onStreakUpdate(streak + 1, today)
                onRecordProgress(habits.count { it.isChecked })
                party = listOf(
                    Party(
                        speed = 0f,
                        maxSpeed = 30f,
                        damping = 0.9f,
                        angle = 270,
                        spread = 360,
                        colors = listOf(0xfce18a, 0xff726d, 0xf44336, 0x9c27b0),
                        emitter = Emitter(duration = 2, TimeUnit.SECONDS).perSecond(50),
                        position = Position.Relative(0.5, 0.3)
                    )
                )
                Toast.makeText(context, AppStrings.get(language, "day_complete"), Toast.LENGTH_LONG).show()
            }
        } else if (!allDone) {
            isDayFinished = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (backgroundImage != null) {
            AsyncImage(model = backgroundImage, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop, alpha = 0.6f)
        } else {
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color(0xFF81D4FA), Color(0xFFC8E6C9)))))
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(AppStrings.get(language, "app_title"), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.DarkGray, textAlign = TextAlign.Center)

            if (currentPhrase != null) {
                InfoCard(
                    title = AppStrings.get(language, "phrase_title"),
                    content = currentPhrase!!.text,
                    icon = Icons.Default.Lightbulb,
                    isFavorite = favorites.any { it.contentText == currentPhrase!!.text },
                    onToggleFavorite = { onToggleFavorite(currentPhrase!!) },
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    onCopy = { copyToClipboard(context, currentPhrase!!.text, language) },
                    onShare = { shareText(context, currentPhrase!!.text) }
                )
            }

            if (currentQuote != null) {
                QuoteCard(
                    language = language,
                    quote = currentQuote!!,
                    isFavorite = favorites.any { it.contentText == currentQuote!!.contentText },
                    onToggleFavorite = { onToggleFavorite(currentQuote!!) },
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    onCopy = { copyToClipboard(context, "${currentQuote!!.text} - ${currentQuote!!.author}", language) },
                    onShare = { shareText(context, "${currentQuote!!.text} - ${currentQuote!!.author}") }
                )
            }

            if (currentFact != null) {
                InfoCard(
                    title = AppStrings.get(language, "fact_title"),
                    content = currentFact!!.text,
                    icon = Icons.Default.Info,
                    isFavorite = favorites.any { it.contentText == currentFact!!.text },
                    onToggleFavorite = { onToggleFavorite(currentFact!!) },
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    onCopy = { copyToClipboard(context, currentFact!!.text, language) },
                    onShare = { shareText(context, currentFact!!.text) }
                )
            }

            Button(
                onClick = { updateContent() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(20.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF64B5F6), Color(0xFF81C784))
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = androidx.compose.ui.res.painterResource(id = R.drawable.self_improvement_24),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = AppStrings.get(language, "refresh").uppercase(),
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp,
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            MissionCard(
                lang = language,
                activeMissions = activeMissions,
                completedMissions = completedMissions,
                onAdd = onAddMission,
                onComplete = { mission ->
                    onCompleteMission(mission)
                    party = listOf(
                        Party(
                            speed = 0f,
                            maxSpeed = 30f,
                            damping = 0.9f,
                            angle = 270,
                            spread = 360,
                            colors = listOf(0xfce18a, 0xff726d, 0xf44336, 0x9c27b0),
                            emitter = Emitter(duration = 1, TimeUnit.SECONDS).perSecond(50),
                            position = Position.Relative(0.5, 0.3)
                        )
                    )
                }
            )

            HabitCard(lang = language, habits = habits, streak = streak, onAdd = onAddHabit, onUpdate = onUpdateHabit, onDelete = onDeleteHabit)
            Spacer(modifier = Modifier.height(20.dp))
        }
        
        if (party.isNotEmpty()) {
            KonfettiView(modifier = Modifier.fillMaxSize(), parties = party)
        }
    }
}

@Composable
fun MissionCard(
    lang: Language,
    activeMissions: List<DailyMission>,
    completedMissions: List<DailyMission>,
    onAdd: (String, String?) -> Unit,
    onComplete: (DailyMission) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf(false) }
    
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(AppStrings.get(lang, "mission_title"), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                IconButton(onClick = { showAddDialog = true }) { Icon(Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.primary) }
            }
            Spacer(modifier = Modifier.height(8.dp))
            
            if (activeMissions.isEmpty()) {
                Text(AppStrings.get(lang, "no_active"), color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
            } else {
                activeMissions.forEach { mission ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                        Checkbox(checked = false, onCheckedChange = { onComplete(mission) })
                        Column {
                            Text(text = if (lang == Language.BG) mission.textBg else mission.textEn, style = MaterialTheme.typography.bodyLarge)
                            if (mission.reminderTime != null) {
                                Text(text = "⏰ ${mission.reminderTime}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                            }
                        }
                    }
                }
            }
            
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
                IconButton(onClick = { showHistoryDialog = true }) { Icon(Icons.Default.History, contentDescription = "Completed", tint = MaterialTheme.colorScheme.secondary) }
            }
        }
    }
    
    if (showAddDialog) {
        MissionInputDialog(lang = lang, onDismiss = { showAddDialog = false }, onConfirm = onAdd)
    }

    if (showHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showHistoryDialog = false },
            title = { Text(AppStrings.get(lang, "achievements")) },
            text = {
                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                    if (completedMissions.isEmpty()) {
                        item { Text(AppStrings.get(lang, "no_completed")) }
                    } else {
                        items(completedMissions) { mission ->
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = if (lang == Language.BG) mission.textBg else mission.textEn, textDecoration = TextDecoration.LineThrough, color = Color.Gray)
                            }
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showHistoryDialog = false }) { Text("OK") } }
        )
    }
}

@Composable
fun MissionInputDialog(lang: Language, onDismiss: () -> Unit, onConfirm: (String, String?) -> Unit) {
    var text by remember { mutableStateOf("") }
    var time by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(AppStrings.get(lang, "edit_mission")) },
        text = {
            Column {
                OutlinedTextField(value = text, onValueChange = { text = it }, modifier = Modifier.fillMaxWidth(), label = { Text(AppStrings.get(lang, "mission_title")) })
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(AppStrings.get(lang, "set_reminder"))
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        val calendar = Calendar.getInstance()
                        TimePickerDialog(context, { _, h, m -> time = String.format(Locale.getDefault(), "%02d:%02d", h, m) }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
                    }) { Text(time ?: "--:--") }
                    if (time != null) {
                        IconButton(onClick = { time = null }) { Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp)) }
                    }
                }
            }
        },
        confirmButton = { Button(onClick = { if (text.isNotBlank()) { onConfirm(text, time); onDismiss() } }) { Text(AppStrings.get(lang, "save")) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(AppStrings.get(lang, "cancel")) } }
    )
}

@Composable
fun HabitCard(lang: Language, habits: List<Habit>, streak: Int, onAdd: (String, String) -> Unit, onUpdate: (Habit) -> Unit, onDelete: (Habit) -> Unit) {
    var showAddDialog by remember { mutableStateOf(false) }
    var habitToEdit by remember { mutableStateOf<Habit?>(null) }
    var habitName by remember { mutableStateOf("") }
    var showHistoryDialog by remember { mutableStateOf(false) }

    val activeHabits = habits.filter { !it.isChecked }
    val completedHabits = habits.filter { it.isChecked }

    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(AppStrings.get(lang, "habits_title"), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    IconButton(onClick = { showAddDialog = true }) { Icon(Icons.Default.Add, contentDescription = "Add Goal", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp)) }
                }
                val streakText = if (lang == Language.BG) {
                    if (streak == 1) "1 ден поред" else "$streak дни поред"
                } else {
                    if (streak == 1) "1 day streak" else "$streak days streak"
                }
                Text("🔥 $streakText", fontWeight = FontWeight.Bold, color = Color(0xFFFF9800))
            }
            Spacer(modifier = Modifier.height(8.dp))
            
            if (activeHabits.isEmpty()) {
                Text(
                    text = if (lang == Language.BG) "С какво ще се борим днес?" else "What are we tackling today?",
                    color = Color.Gray,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                activeHabits.forEach { habit ->
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Checkbox(checked = habit.isChecked, onCheckedChange = { onUpdate(habit.copy(isChecked = it)) })
                            Text(if (lang == Language.BG) habit.nameBg else habit.nameEn)
                        }
                        Row {
                            IconButton(onClick = { habitToEdit = habit }) { Icon(Icons.Default.Edit, contentDescription = "Edit Goal", tint = Color.Gray.copy(alpha = 0.6f), modifier = Modifier.size(18.dp)) }
                            IconButton(onClick = { onDelete(habit) }) { Icon(Icons.Default.Delete, contentDescription = "Delete Goal", tint = Color.Gray.copy(alpha = 0.6f), modifier = Modifier.size(18.dp)) }
                        }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
                IconButton(onClick = { showHistoryDialog = true }) { Icon(Icons.Default.History, contentDescription = "Completed", tint = MaterialTheme.colorScheme.secondary) }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(AppStrings.get(lang, "add_goal")) },
            text = { OutlinedTextField(value = habitName, onValueChange = { habitName = it }, label = { Text(AppStrings.get(lang, "goal_name")) }, modifier = Modifier.fillMaxWidth()) },
            confirmButton = { Button(onClick = { if (habitName.isNotBlank()) { onAdd(habitName, habitName); habitName = ""; showAddDialog = false } }) { Text(AppStrings.get(lang, "save")) } },
            dismissButton = { TextButton(onClick = { showAddDialog = false; habitName = "" }) { Text(AppStrings.get(lang, "cancel")) } }
        )
    }

    if (habitToEdit != null) {
        var editName by remember { mutableStateOf(if (lang == Language.BG) habitToEdit!!.nameBg else habitToEdit!!.nameEn) }
        AlertDialog(
            onDismissRequest = { habitToEdit = null },
            title = { Text(if (lang == Language.BG) "Редактирай цел" else "Edit Goal") },
            text = { OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text(AppStrings.get(lang, "goal_name")) }, modifier = Modifier.fillMaxWidth()) },
            confirmButton = { 
                Button(onClick = { 
                    if (editName.isNotBlank()) { 
                        onUpdate(habitToEdit!!.copy(nameBg = editName, nameEn = editName))
                        habitToEdit = null 
                    } 
                }) { Text(AppStrings.get(lang, "save")) } 
            },
            dismissButton = { TextButton(onClick = { habitToEdit = null }) { Text(AppStrings.get(lang, "cancel")) } }
        )
    }

    if (showHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showHistoryDialog = false },
            title = { Text(AppStrings.get(lang, "completed")) },
            text = {
                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                    if (completedHabits.isEmpty()) {
                        item { Text(if (lang == Language.BG) "Няма завършени цели още." else "No completed goals yet.") }
                    } else {
                        items(completedHabits) { habit ->
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                                Checkbox(checked = true, onCheckedChange = { onUpdate(habit.copy(isChecked = it)) })
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (lang == Language.BG) habit.nameBg else habit.nameEn,
                                    textDecoration = TextDecoration.LineThrough,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showHistoryDialog = false }) { Text("OK") } }
        )
    }
}

@Composable
fun InfoCard(title: String, content: String, icon: ImageVector, isFavorite: Boolean, onToggleFavorite: () -> Unit, containerColor: Color, onCopy: () -> Unit, onShare: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 150.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(6.dp).size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(title.uppercase(), fontWeight = FontWeight.ExtraBold, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, letterSpacing = 0.5.sp)
                }
                Row {
                    IconButton(onClick = onCopy, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)) }
                    IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)) }
                    IconButton(onClick = onToggleFavorite, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = if (isFavorite) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = content, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, lineHeight = 22.sp, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
fun QuoteCard(language: Language, quote: Quote, isFavorite: Boolean, onToggleFavorite: () -> Unit, containerColor: Color, onCopy: () -> Unit, onShare: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 180.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.FormatQuote, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(6.dp).size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(AppStrings.get(language, "quote_title").uppercase(), fontWeight = FontWeight.ExtraBold, fontSize = 10.sp, color = MaterialTheme.colorScheme.secondary, letterSpacing = 0.5.sp)
                }
                Row {
                    IconButton(onClick = onCopy, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)) }
                    IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)) }
                    IconButton(onClick = onToggleFavorite, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = if (isFavorite) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = quote.text, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic, lineHeight = 24.sp, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "— ${quote.author}", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
fun FavoritesScreen(language: Language, favoriteItems: List<FavoriteItem>, onRemove: (String) -> Unit) {
    val groupedFavorites = favoriteItems.groupBy { it.typeKey }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(AppStrings.get(language, "fav"), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(bottom = 16.dp))
        if (favoriteItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(AppStrings.get(language, "no_favs"), color = Color.Gray) }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                groupedFavorites.forEach { (typeKey, items) ->
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = AppStrings.get(language, typeKey),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Column(
                                    modifier = Modifier
                                        .heightIn(max = 300.dp)
                                        .verticalScroll(rememberScrollState())
                                        .fillMaxWidth()
                                ) {
                                    items.forEachIndexed { index, item ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = item.contentText,
                                                modifier = Modifier.weight(1f),
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            IconButton(onClick = { onRemove(item.contentText) }) {
                                                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.Gray.copy(alpha = 0.6f))
                                            }
                                        }
                                        if (index < items.size - 1) {
                                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(language: Language, onLanguageChange: (Language) -> Unit, isDarkMode: Boolean, onThemeToggle: (Boolean) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(AppStrings.get(language, "settings"), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(bottom = 24.dp))
        SettingsItem(AppStrings.get(language, "notif_label"), Icons.Default.Notifications) {
            var checked by remember { mutableStateOf(true) }
            Switch(checked = checked, onCheckedChange = { checked = it })
        }
        SettingsItem(AppStrings.get(language, "theme_label"), Icons.Default.DarkMode) { Switch(checked = isDarkMode, onCheckedChange = onThemeToggle) }
        SettingsItem(AppStrings.get(language, "lang_label"), Icons.Default.Language) {
            Row {
                TextButton(onClick = { onLanguageChange(Language.BG) }, colors = ButtonDefaults.textButtonColors(contentColor = if (language == Language.BG) MaterialTheme.colorScheme.primary else Color.Gray)) { Text("BG") }
                TextButton(onClick = { onLanguageChange(Language.EN) }, colors = ButtonDefaults.textButtonColors(contentColor = if (language == Language.EN) MaterialTheme.colorScheme.primary else Color.Gray)) { Text("EN") }
            }
        }
    }
}

@Composable
fun SettingsItem(title: String, icon: ImageVector, control: @Composable () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, fontSize = 18.sp)
        }
        control()
    }
}

fun copyToClipboard(context: Context, text: String, lang: Language) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Daily Motivator", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, AppStrings.get(lang, "copy_toast"), Toast.LENGTH_SHORT).show()
}

fun shareText(context: Context, text: String) {
    val shareContent = """
        ✨ Daily Motivator ✨
        
        "$text"
        
        Sent from my Daily Motivator app 🔥
    """.trimIndent()
    
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareContent)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DailyMotivatorTheme {
        DailyMotivatorApp(false, {})
    }
}
