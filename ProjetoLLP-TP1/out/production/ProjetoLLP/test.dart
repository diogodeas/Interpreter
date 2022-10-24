// Ler uma quantidade de jogadas do teclado.
var? tmp = read('Entre com uma quantidade de jogadas de dados: ');
final var runs = toint(tmp ?? '0');
assert(runs > 0, 'Quantidade de jogadas invalida');
// Sortear rodadas aleatorias do dado.
var i = 0, r, freqs = {};
while (i++ < runs) {
 r = random(6) + 1;
 if (freqs[r] == null)
 freqs[r] = 1;
 else
 freqs[r]++;
}
// Sumarizar as frequencias obtidas em listas.
var side;
final var halve1 = [for (side in [1,2,3]) freqs[side] ?? 0];
final var halve2 = [for (side in [4,5,6]) freqs[side] ?? 0];
final var all = [...halve1, ...halve2];
// Imprimir os resultados.
print('Primeira metade: ' + tostr(halve1));
print('Segunda metade: ' + tostr(halve2));
print('Todos: ' + tostr(all));