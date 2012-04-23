For this problem, I present two solutions: the first how I interpret the
original problem, and the second as an improvement.

For the first solution, I interpreted the problem as requiring the patterns
of letters and digits require EVERY digit 0-9 and EVERY letter A-Z to be
used.  This can lead to an obvious creation of too many plates, as well
as awkward amounts of extra plates for population values 1 apart, i.e.
a population of 260 would simply have license plates with A-Z and 0-9,
but a population of 261 would have license plates with two letters A-Z,
creating a total of 676 licence plates, which is 415 more than we want,
and over 150% of the population.  So, a better solution is needed.

In the second solution, we first check for parity, and if even immediately
attempt dividing by 26 or 10.  Otherwise, we attempt to find a smaller number
that divides it, and have our license plate pattern only go up to a smaller
number of digits or letters.  For example, a population of 25 will give us
two digits, each 0-4, with zero extra.  This solution will have much fewer
extra license plates, and will generally only create an extra when the algorithm
is forced to work with a prime number greater than 26.
