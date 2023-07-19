import 'package:flutter/material.dart';
import 'package:plugpag_flutter/plugpag_flutter_method_channel.dart';
import 'package:plugpag_flutter/plugpag_flutter_platform_interface.dart';

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      home: Demo(),
    );
  }
}

class Demo extends StatefulWidget {
  const Demo({
    super.key,
  });

  @override
  State<Demo> createState() => _DemoState();
}

class _DemoState extends State<Demo> {
  late PlugpagFlutterPlatform plugpag;
  @override
  void initState() {
    super.initState();
    plugpag = PlugpagFlutterPlatform.instance = MethodChannelPlugpagFlutter(onState: onState);
  }

  void onState(StatesPlugPag state) async {
    if (Navigator.canPop(context)) {
      Navigator.pop(context);
    }
    await showDialog(
      context: context,
      builder: (context) => AlertDialog(
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            if (state.type == Type.loading) const CircularProgressIndicator(),
            Text(state.message),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('PagSeguro - Plugpag'),
      ),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Text(
                "Antes de tudo, faça o pareamento da sua máquina de cartão!",
                textAlign: TextAlign.center,
                style: TextStyle(color: Colors.red, fontSize: 20),
              ),
              const SizedBox(height: 20),
              const Text("Execute os botões na ordem:"),
              const SizedBox(height: 30),
              TextButton(
                child: const Text("Pedir permissões"),
                onPressed: () async {
                  await plugpag.requestPermissions();
                },
              ),
              TextButton(
                child: const Text("Autenticar no PagSeguro"),
                onPressed: () async {
                  await plugpag.requestAuthentication();
                },
              ),
              TextButton(
                child: const Text("Verificar o status da autenticacao"),
                onPressed: () async {
                  var result = await plugpag.checkAuthentication();
                  print(result);
                },
              ),
              const SizedBox(height: 30),
              const Text("Se for uma minizinha ou similares:"),
              TextButton(
                child: const Text("Realizar uma transação de débito no valor de R\$: 2,00"),
                onPressed: () async {
                  await plugpag.startPinpadDebitPayment(2.00);
                },
              ),
              const SizedBox(height: 20),
              const Text("Se for uma a pro ou similares:"),
              TextButton(
                child: const Text("Realizar uma transação de débito no valor de R\$: 2,00"),
                onPressed: () async {
                  await plugpag.startTerminalDebitPayment(2.00);
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}
